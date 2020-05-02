package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagingData
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sournary.architecturecomponent.api.MovieDbApi
import com.sournary.architecturecomponent.api.MovieListResponse
import com.sournary.architecturecomponent.db.GenreDao
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

/**
 * The repository class for movie.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class MovieRepository(private val movieDbApi: MovieDbApi, private val genreDao: GenreDao) {

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

    fun getMovies(genre: Genre): LiveData<PagingData<Movie>> = LivePagingData(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        pagingSourceFactory = {
            MovieDataSource { page -> getMoviesOfGenre(page, genre) }
        }
    )

    private suspend fun getMoviesOfGenre(page: Int, genre: Genre): MovieListResponse =
        when (genre) {
            Genre.SAVED_GENRES[0] -> movieDbApi.getNowPlayingMovies(page)
            Genre.SAVED_GENRES[1] -> movieDbApi.getPopularMovies(page)
            Genre.SAVED_GENRES[2] -> movieDbApi.getTopRatedMovies(page)
            Genre.SAVED_GENRES[3] -> movieDbApi.getUpcomingMovies(page)
            else -> movieDbApi.getMoviesByGenre(page, genre.id)
        }

    fun getRatedMovies(movieId: Int): LiveData<PagingData<Movie>> = LivePagingData(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        pagingSourceFactory = {
            MovieDataSource { page -> movieDbApi.getRelatedMovies(movieId, page) }
        }
    )

    //====================== Flow version =======================//

    val genresUsingFlow: Flow<List<Genre>>
        get() = genreDao.getFlowGenres()
            .combine(::getRemoteGenres.asFlow()) { localGenres, remoteGenres ->
                val result = arrayListOf<Genre>()
                result.addAll(localGenres)
                result.addAll(remoteGenres)
                result
            }
            .flowOn(Dispatchers.IO)
            .conflate()

    private suspend fun getRemoteGenres(): List<Genre> = try {
        movieDbApi.getGenres().genres ?: emptyList()
    } catch (throwable: Throwable) {
        emptyList()
    }

    companion object {

        private const val DEF_ERROR = "Unknown error"
        private const val NETWORK_PAGE_SIZE = 50

    }

}
