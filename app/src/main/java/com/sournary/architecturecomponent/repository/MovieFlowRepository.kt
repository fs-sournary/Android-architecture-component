package com.sournary.architecturecomponent.repository

import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.sournary.architecturecomponent.api.MovieDbApi
import com.sournary.architecturecomponent.api.MovieListResponse
import com.sournary.architecturecomponent.db.GenreDao
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

/**
 * The movie repository uses Flow api.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class MovieFlowRepository(private val movieDbApi: MovieDbApi, private val genreDao: GenreDao) {

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

    fun getFlowMovies(scope: CoroutineScope, genre: Genre): Listing<Movie> {
        val factory = MovieFlowDataSource.Factory(
            block = { page -> getMoviesOfGenre(page, genre) },
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

    private suspend fun getMoviesOfGenre(page: Int, genre: Genre): MovieListResponse =
        when (genre) {
            Genre.SAVED_GENRES[0] -> movieDbApi.getNowPlayingMovies(page)
            Genre.SAVED_GENRES[1] -> movieDbApi.getPopularMovies(page)
            Genre.SAVED_GENRES[2] -> movieDbApi.getTopRatedMovies(page)
            Genre.SAVED_GENRES[3] -> movieDbApi.getUpcomingMovies(page)
            else -> movieDbApi.getMoviesByGenre(page, genre.id)
        }

}
