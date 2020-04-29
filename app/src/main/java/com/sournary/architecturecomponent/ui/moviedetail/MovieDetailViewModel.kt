package com.sournary.architecturecomponent.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sournary.architecturecomponent.data.Movie
import com.sournary.architecturecomponent.repository.DataState
import com.sournary.architecturecomponent.repository.MovieRepository
import kotlinx.coroutines.launch

/**
 * The view model class contains all logic of movie detail screen.
 */
class MovieDetailViewModel(private val movieId: Int, private val movieRepository: MovieRepository) :
    ViewModel() {

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState>
        get() = _dataState

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val relatedMoviesRepoResult = movieRepository.getRatedMovies(viewModelScope, movieId)
    val relatedMovies = relatedMoviesRepoResult.data
    val relatedMovieState = relatedMoviesRepoResult.dataState

    init {
        loadMovie()
    }

    fun loadMovie() {
        viewModelScope.launch {
            try {
                _dataState.value = DataState.LOADING
                _movie.value = movieRepository.getMovieDetail(movieId)
                _dataState.value = DataState.SUCCESS
            } catch (throwable: Throwable) {
                _dataState.value = DataState.error(throwable.message ?: "Unknown error")
            }
        }
    }

    fun retryGetRelatedMovies(){
        relatedMoviesRepoResult.retry?.invoke()
    }

}
