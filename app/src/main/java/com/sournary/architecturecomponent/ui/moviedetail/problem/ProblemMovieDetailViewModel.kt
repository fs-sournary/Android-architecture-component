package com.sournary.architecturecomponent.ui.moviedetail.problem

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sournary.architecturecomponent.repository.home.HomeRepository
import com.sournary.architecturecomponent.repository.result.RepoResult
import com.sournary.architecturecomponent.repository.result.data
import com.sournary.architecturecomponent.repository.result.error

/**
 * The view model class contains all logic of movie detail screen.
 */
class ProblemMovieDetailViewModel @ViewModelInject constructor(
    homeRepository: HomeRepository
) : ViewModel() {

    // Fixme: problem 4: Leaking ViewModel
    private val swipeRefreshLayoutListener = SwipeRefreshLayout.OnRefreshListener {

    }

    private val _movieId = MutableLiveData<Int>()
    private val movieResult = _movieId.switchMap { homeRepository.getMovieDetail(it) }
    val movie = movieResult.map { it.data }
    val loading = movieResult.map { it is RepoResult.Loading }
    val error = movieResult.map { it.error }

    // val relatedMovies = homeRepository.get

    fun setMovieId(movieId: Int) {
        // Fixme: problem 3: Reload data each time navigation or device rotation
        _movieId.value = movieId
    }

    fun retryGetMovie(movieId: Int) {
        _movieId.value = movieId
    }
}
