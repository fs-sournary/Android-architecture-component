package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.sournary.architecturecomponent.api.MovieDbApi
import com.sournary.architecturecomponent.api.MovieListResponse
import com.sournary.architecturecomponent.db.GenreDao
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.model.Video
import kotlinx.coroutines.CoroutineScope

/**
 * The repository class for movie.
 */
class MovieRepository(private val movieDbApi: MovieDbApi, genreDao: GenreDao) {

    val genres: LiveData<List<Genre>> = genreDao.getGenres()
        .switchMap { localGenres ->
            liveData {
                val results = arrayListOf<Genre>()
                results.addAll(localGenres)
                val serverGenres = try {
                    movieDbApi.getGenres().genres
                } catch (throwable: Throwable) {
                    null
                }
                serverGenres?.let { results.addAll(it) }
                emit(results.toList())
            }
        }

    fun getVideos(movieId: Int): LiveData<RepoResult<List<Video>>> = liveData {
        try {
            emit(RepoResult(networkState = NetworkState.LOADING))
            val videos = movieDbApi.getVideos(movieId).results ?: emptyList()
            emit(RepoResult(data = videos, networkState = NetworkState.SUCCESS))
        } catch (throwable: Throwable) {
            val errorState = NetworkState.error(throwable.message ?: DEF_ERROR)
            emit(RepoResult(networkState = errorState))
        }
    }

    fun getMovies(scope: CoroutineScope, genre: Genre): Listing<Movie> {
        val dataSourceFactory = MovieDataSource.Factory(
            block = { page -> getMoviesOfGenre(page, genre) },
            scope = scope
        )
        val movies = dataSourceFactory.toLiveData(pageSize = 30)
        return Listing(
            data = movies,
            networkState = dataSourceFactory.sourceLiveData.switchMap { it.networkState },
            refreshState = dataSourceFactory.sourceLiveData.switchMap { it.refreshState },
            refresh = { dataSourceFactory.sourceLiveData.value?.invalidate() },
            retry = { dataSourceFactory.sourceLiveData.value?.retryWhenAllFailed() }
        )
    }

    private suspend fun getMoviesOfGenre(page: Int, genre: Genre): MovieListResponse =
        when (genre) {
            Genre.SAVED_GENRES[0] -> movieDbApi.getNowPlayingMovies(page)
            Genre.SAVED_GENRES[1] -> movieDbApi.getPopularMovies(page)
            Genre.SAVED_GENRES[2] -> movieDbApi.getTopRatedMovies(page)
            Genre.SAVED_GENRES[3] -> movieDbApi.getUpcomingMovies(page)
            else -> movieDbApi.getMoviesByGenre(page, genre.id)
        }

    fun getMovieDetail(id: Int): LiveData<RepoResult<Movie>> = liveData {
        try {
            emit(RepoResult(networkState = NetworkState.LOADING))
            val movie = movieDbApi.getMovieDetail(id)
            emit(RepoResult(data = movie, networkState = NetworkState.SUCCESS))
        } catch (throwable: Throwable) {
            val errorState = NetworkState.error(throwable.message ?: DEF_ERROR)
            emit(RepoResult(networkState = errorState))
        }
    }

    fun getRatedMovies(scope: CoroutineScope, movieId: Int): Listing<Movie> {
        val factory = MovieDataSource.Factory(
            block = { page -> movieDbApi.getRelatedMovies(movieId, page) },
            scope = scope
        )
        val movies = factory.toLiveData(pageSize = 30)
        return Listing(
            data = movies,
            networkState = factory.sourceLiveData.switchMap { it.networkState },
            refreshState = factory.sourceLiveData.switchMap { it.refreshState },
            retry = { factory.sourceLiveData.value?.retryWhenAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() }
        )
    }

    companion object {

        private const val DEF_ERROR = "Unknown error"

    }

}
