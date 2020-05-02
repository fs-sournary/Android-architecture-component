package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.api.MovieListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * The class has same function with [MovieDataSource] but main purpose is to use Flow with coroutine.
 */
@ExperimentalCoroutinesApi
class MovieFlowDataSource(
    private val block: suspend (Int) -> MovieListResponse,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Movie>() {

    private var retry: (() -> Unit)? = null

    private val _dateState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _dateState

    private val _refreshState = MutableLiveData<NetworkState>()
    val refreshState: LiveData<NetworkState>
        get() = _refreshState

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        flowOf(1).onStart {
            _dateState.postValue(NetworkState.LOADING)
            _refreshState.postValue(NetworkState.LOADING)
        }.mapLatest { page ->
            val result = block.invoke(page)
            val movies = result.results ?: emptyList()
            _dateState.postValue(NetworkState.SUCCESS)
            _refreshState.postValue(NetworkState.SUCCESS)
            retry = null
            callback.onResult(movies, null, 2)
        }.catch { throwable ->
            val error = NetworkState.error(throwable.message ?: DEF_ERROR_MESSAGE)
            _dateState.postValue(error)
            _refreshState.postValue(error)
            retry = { loadInitial(params, callback) }
        }.launchIn(scope)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        flowOf(params.key).onStart {
            _dateState.postValue(NetworkState.LOADING)
        }.mapLatest { page ->
            val result = block.invoke(page)
            val movies = result.results ?: emptyList()
            _dateState.postValue(NetworkState.SUCCESS)
            retry = null
            val nextPage = if (page == result.totalPage) null else page + 1
            callback.onResult(movies, nextPage)
        }.catch { throwable ->
            _dateState.postValue(NetworkState.error(throwable.message ?: DEF_ERROR_MESSAGE))
            retry = { loadAfter(params, callback) }
        }.launchIn(scope)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        // Ignored
    }

    fun retryWhenAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    companion object {

        private const val DEF_ERROR_MESSAGE = "Unknown error"

    }

    class Factory(
        private val block: suspend (Int) -> MovieListResponse,
        private val scope: CoroutineScope
    ) : DataSource.Factory<Int, Movie>() {

        private val _sourceLiveData = MutableLiveData<MovieFlowDataSource>()
        val sourceLiveData: LiveData<MovieFlowDataSource>
            get() = _sourceLiveData

        override fun create(): DataSource<Int, Movie> {
            val source = MovieFlowDataSource(block, scope)
            _sourceLiveData.postValue(source)
            return source
        }

    }

}
