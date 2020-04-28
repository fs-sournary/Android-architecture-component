package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.sournary.architecturecomponent.api.MovieDbApi
import com.sournary.architecturecomponent.data.Genre
import com.sournary.architecturecomponent.data.Movie
import com.sournary.architecturecomponent.data.MovieListResponse
import com.sournary.architecturecomponent.db.GenreDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * The repository class for movie.
 */
class MovieRepository(
    private val movieDbApi: MovieDbApi,
    genreDao: GenreDao
) {

    //-- Using liveData{} builder --//
    val genres: LiveData<List<Genre>> = liveData {
        val results = arrayListOf<Genre>()
        val serverGenres = try {
            movieDbApi.getGenres().genres
        } catch (throwable: Throwable) {
            null
        }
        val localGenres = genreDao.getGenres()
        emitSource(localGenres.map {
            results.addAll(it)
            serverGenres?.apply { results.addAll(this) }
            results.toList()
        })
    }
    //-- Using switchMap + liveData{} builder--//
    val genresUsingSwitchMap: LiveData<List<Genre>> = genreDao.getGenres()
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

    //-- Only using coroutine --//
    fun getMovies(scope: CoroutineScope, genre: Genre): Listing<Movie> {
        val dataSourceFactory = MovieDataSource.Factory(
            block = { page -> getMoviesOfGenre(page, genre) },
            scope = scope
        )
        val movies = dataSourceFactory.toLiveData(pageSize = 30)
        return Listing(
            data = movies,
            dataState = dataSourceFactory.sourceLiveData.switchMap { it.networkState },
            refreshState = dataSourceFactory.sourceLiveData.switchMap { it.refreshState },
            refresh = { dataSourceFactory.sourceLiveData.value?.invalidate() },
            retry = { dataSourceFactory.sourceLiveData.value?.retryWhenAllFailed() }
        )
    }

    //-- Using flow with coroutine --//
    @ExperimentalCoroutinesApi
    fun getFlowMovies(scope: CoroutineScope, genre: Genre): Listing<Movie> {
        val factory = MovieFlowDataSource.Factory(
            block = { page -> getMoviesOfGenre(page, genre) },
            scope = scope
        )
        val movies = factory.toLiveData(pageSize = 30)
        return Listing(
            data = movies,
            dataState = factory.sourceLiveData.switchMap { it.dataState },
            refreshState = factory.sourceLiveData.switchMap { it.refreshState },
            retry = { factory.sourceLiveData.value?.retryWhenAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() }
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

}
