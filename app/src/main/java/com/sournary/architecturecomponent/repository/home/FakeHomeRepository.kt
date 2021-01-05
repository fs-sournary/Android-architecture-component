package com.sournary.architecturecomponent.repository.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.sournary.architecturecomponent.data.api.MovieDbApi
import com.sournary.architecturecomponent.data.movie.MovieDataSource
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.model.MovieListResponse
import com.sournary.architecturecomponent.repository.result.RepoResult
import com.sournary.architecturecomponent.util.Constant

/**
 * The fake home repository is responsible for testing.
 */
class FakeHomeRepository(private val movieDbApi: MovieDbApi) : HomeRepository {

    override fun getGenres(): LiveData<List<Genre>> = liveData { }

    override fun getMovieDetail(id: Int): LiveData<RepoResult<Movie>> = liveData { }

    override fun getMovies(genre: Genre): LiveData<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = Constant.NETWORK_PAGE_SIZE),
        pagingSourceFactory = { MovieDataSource { page -> getMoviesOfGenre(page, genre) } }
    ).liveData

    private suspend fun getMoviesOfGenre(page: Int, genre: Genre): MovieListResponse =
        when (genre) {
            Genre.SAVED_GENRES[0] -> movieDbApi.getNowPlayingMovies(page)
            Genre.SAVED_GENRES[1] -> movieDbApi.getPopularMovies(page)
            Genre.SAVED_GENRES[2] -> movieDbApi.getTopRatedMovies(page)
            Genre.SAVED_GENRES[3] -> movieDbApi.getUpcomingMovies(page)
            else -> movieDbApi.getMoviesByGenre(page, genre.id)
        }
}
