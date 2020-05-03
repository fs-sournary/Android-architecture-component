package com.sournary.architecturecomponent.repository

import androidx.paging.PagingSource
import com.sournary.architecturecomponent.api.MovieListResponse
import com.sournary.architecturecomponent.model.Movie

/**
 * The data source fetches movies from server by using Paging and Coroutine.
 *
 * @param block A suspending function fetches movies and return movie response.
 */
class MovieDataSource(private val block: suspend (Int) -> MovieListResponse) :
    PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val currentPage = params.key ?: START_PAGE_INDEX
        return try {
            val response = block.invoke(currentPage)
            val movies = response.results ?: emptyList()
            return LoadResult.Page(
                data = movies,
                prevKey = if (currentPage == START_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (currentPage == response.totalPage) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {

        private const val START_PAGE_INDEX = 1

    }

}
