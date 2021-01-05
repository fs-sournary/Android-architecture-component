package com.sournary.architecturecomponent.ui.moviedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.sournary.architecturecomponent.repository.home.HomeRepository
import com.sournary.architecturecomponent.repository.result.RepoResult
import com.sournary.architecturecomponent.repository.result.data
import com.sournary.architecturecomponent.repository.result.error

/**
 * The view model class contains all logic of movie detail screen.
 */
class MovieDetailViewModel @ViewModelInject constructor(
    homeRepository: HomeRepository
) : ViewModel() {

    private val _movieId = MutableLiveData<Int>()
    private val movieResult = _movieId.switchMap { homeRepository.getMovieDetail(it) }
    val movie = movieResult.map { it.data }
    val loading = movieResult.map { it is RepoResult.Loading }
    val error = movieResult.map { it.error }

    // val relatedMovies = homeRepository.get

    fun setMovieId(movieId: Int){
        if (_movieId.value != movieId){
            _movieId.value = movieId
        }
    }

    fun retryGetMovie(movieId: Int) {
        _movieId.value = movieId
    }
}
