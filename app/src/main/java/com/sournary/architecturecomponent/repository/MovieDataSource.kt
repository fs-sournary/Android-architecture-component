package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.sournary.architecturecomponent.data.Movie
import com.sournary.architecturecomponent.data.MovieListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The data source fetches movies from server by using Paging and Coroutine.
 *
 * @param block A suspending function fetches movies and return movie response.
 * @param scope A coroutine scope that the fetched job should run on.
 */
class MovieDataSource(
    private val block: suspend (Int) -> MovieListResponse,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Movie>() {

    private var retry: (() -> Any)? = null

    private val _networkState = MutableLiveData<DataState>()
    val networkState: LiveData<DataState>
        get() = _networkState

    private val _refreshState = MutableLiveData<DataState>()
    val refreshState: LiveData<DataState>
        get() = _refreshState

    fun retryWhenAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        scope.launch {
            try {
                _networkState.postValue(DataState.LOADING)
                _refreshState.postValue(DataState.LOADING)
                val response = block.invoke(1)
                val movies = response.results ?: emptyList()
                retry = null
                _networkState.postValue(DataState.SUCCESS)
                _refreshState.postValue(DataState.SUCCESS)
                callback.onResult(movies, null, 2)
            } catch (e: Exception) {
                val error = DataState.error(e.message ?: DEF_ERROR)
                _networkState.postValue(error)
                _refreshState.postValue(error)
                retry = { loadInitial(params, callback) }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        scope.launch {
            try {
                _networkState.value = DataState.LOADING
                val response = block.invoke(params.key)
                val movies = response.results ?: emptyList()
                retry = null
                val nextKey = if (params.key == response.totalPage) null else params.key + 1
                _networkState.postValue(DataState.SUCCESS)
                callback.onResult(movies, nextKey)
            } catch (e: Exception) {
                _networkState.postValue(DataState.error(e.message ?: DEF_ERROR))
                retry = { loadAfter(params, callback) }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        // Ignored
    }

    companion object {

        private const val DEF_ERROR = "Unknown error"

    }

    class Factory(
        private val block: suspend (Int) -> MovieListResponse,
        private val scope: CoroutineScope
    ) : DataSource.Factory<Int, Movie>() {

        private val _sourceLiveData = MutableLiveData<MovieDataSource>()
        val sourceLiveData: LiveData<MovieDataSource>
            get() = _sourceLiveData

        override fun create(): DataSource<Int, Movie> {
            val source = MovieDataSource(block, scope)
            _sourceLiveData.postValue(source)
            return source
        }

    }

}
