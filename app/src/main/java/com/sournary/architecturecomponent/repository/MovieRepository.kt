package com.sournary.architecturecomponent.repository

import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.sournary.architecturecomponent.api.MovieDbApi
import com.sournary.architecturecomponent.data.Genre
import com.sournary.architecturecomponent.data.Movie
import com.sournary.architecturecomponent.data.MovieCategory
import com.sournary.architecturecomponent.data.MovieListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * The repository class for movie.
 */
class MovieRepository(private val movieDbApi: MovieDbApi) {

    suspend fun getGenres(): List<Genre> {
        val response = movieDbApi.getGenres()
        return response.genres ?: emptyList()
    }

    fun getMovies(scope: CoroutineScope, category: String): Listing<Movie> {
        val dataSourceFactory = MovieDataSource.Factory(
            block = { page -> getMoviesByCategory(page, category) },
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

    private suspend fun getMoviesByCategory(page: Int, category: String): MovieListResponse =
        when (category) {
            MovieCategory.NOW_PLAYING.value -> movieDbApi.getNowPlayingMovies(page)
            MovieCategory.POPULAR.value -> movieDbApi.getPopularMovies(page)
            MovieCategory.TOP_RATED.value -> movieDbApi.getTopRatedMovies(page)
            MovieCategory.UP_COMING.value -> movieDbApi.getUpcomingMovies(page)
            else -> movieDbApi.getNowPlayingMovies(page)
        }

    //-- Using flow --//

    @ExperimentalCoroutinesApi
    fun getNowPlayingFlowMovies(scope: CoroutineScope): Listing<Movie> {
        val factory = MovieFlowDataSource.Factory(
            block = { page -> movieDbApi.getNowPlayingMovies(page) },
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

}
