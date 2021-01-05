package com.sournary.architecturecomponent.repository.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.sournary.architecturecomponent.data.api.MovieDbApi
import com.sournary.architecturecomponent.data.db.GenreDao
import com.sournary.architecturecomponent.data.movie.MovieDataSource
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.model.MovieListResponse
import com.sournary.architecturecomponent.repository.result.RepoResult
import com.sournary.architecturecomponent.util.Constant.NETWORK_PAGE_SIZE

class DefaultHomeRepository(
    private val movieDbApi: MovieDbApi,
    private val genreDao: GenreDao
) : HomeRepository {

    override fun getGenres(): LiveData<List<Genre>> = genreDao.getGenres()
        .switchMap { localGenres ->
            liveData {
                val result = arrayListOf<Genre>()
                result.addAll(localGenres)
                val remoteGenres = try {
                    movieDbApi.getGenres().genres
                } catch (e: Exception) {
                    null
                }
                remoteGenres?.let { result.addAll(it) }
                emit(result.toList())
            }
        }

    override fun getMovieDetail(id: Int): LiveData<RepoResult<Movie>> = liveData {
        try {
            emit(RepoResult.Loading)
            val movie = movieDbApi.getMovieDetail(id)
            emit(RepoResult.Success(movie))
        } catch (e: Exception) {
            emit(RepoResult.Error(e))
        }
    }

    override fun getMovies(genre: Genre): LiveData<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
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