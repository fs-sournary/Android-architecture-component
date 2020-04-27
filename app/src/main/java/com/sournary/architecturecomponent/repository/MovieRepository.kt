package com.sournary.architecturecomponent.repository

import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.sournary.architecturecomponent.api.MovieDbApi
import com.sournary.architecturecomponent.data.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * The repository class for movie.
 */
class MovieRepository(private val movieDbApi: MovieDbApi) {

    fun getNowPlayingMovies(scope: CoroutineScope): RepoResult<PagedList<Movie>> {
        val dataSourceFactory = MovieDataSource.Factory(
            block = { page -> movieDbApi.getNowPlayingMovies(page) },
            scope = scope
        )
        val movies = dataSourceFactory.toLiveData(pageSize = 30)
        return RepoResult(
            data = movies,
            dataState = dataSourceFactory.sourceLiveData.switchMap { it.networkState },
            refreshState = dataSourceFactory.sourceLiveData.switchMap { it.refreshState },
            refresh = { dataSourceFactory.sourceLiveData.value?.invalidate() },
            retry = { dataSourceFactory.sourceLiveData.value?.retryWhenAllFailed() }
        )
    }

    @ExperimentalCoroutinesApi
    fun getNowPlayingFlowMovies(scope: CoroutineScope): RepoResult<PagedList<Movie>> {
        val factory = MovieFlowDataSource.Factory(
            block = { page -> movieDbApi.getNowPlayingMovies(page) },
            scope = scope
        )
        val movies = factory.toLiveData(pageSize = 30)
        return RepoResult(
            data = movies,
            dataState = factory.sourceLiveData.switchMap { it.dataState },
            refreshState = factory.sourceLiveData.switchMap { it.refreshState },
            retry = { factory.sourceLiveData.value?.retryWhenAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() }
        )
    }

}
