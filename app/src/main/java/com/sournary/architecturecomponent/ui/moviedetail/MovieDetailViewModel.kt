package com.sournary.architecturecomponent.ui.moviedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.sournary.architecturecomponent.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * The view model class contains all logic of movie detail screen.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class MovieDetailViewModel(private val movieId: Int, movieRepository: MovieRepository) :
    ViewModel() {

    private val _movieId = MutableLiveData<Int>(movieId)
    private val movieRepoResult = _movieId.switchMap { movieRepository.getMovieDetail(movieId) }
    val movie = movieRepoResult.map { it.data }
    val movieNetworkState = movieRepoResult.map { it.networkState }

    val relatedMovies = movieRepository.getRatedMovies(movieId)

    fun retryGetMovie() {
        _movieId.value = movieId
    }

}
