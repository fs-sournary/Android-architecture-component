package com.sournary.architecturecomponent.ui.home

import androidx.lifecycle.*
import com.sournary.architecturecomponent.data.MovieCategory
import com.sournary.architecturecomponent.repository.MovieRepository

/**
 * The view model contains all logic of home screen.
 */
class HomeViewModel(
    movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val category = savedStateHandle.getLiveData<String>(KEY_MOVIE_CATEGORY)
    private val moviesRepoResult = category.map {
        movieRepository.getMovies(viewModelScope, it)
    }
    val movies = moviesRepoResult.switchMap { it.data }
    val dataState = moviesRepoResult.switchMap { it.dataState!! }
    val refreshState = moviesRepoResult.switchMap { it.refreshState!! }

    init {
        if (!savedStateHandle.contains(KEY_MOVIE_CATEGORY)) {
            savedStateHandle.set(KEY_MOVIE_CATEGORY, MovieCategory.NOW_PLAYING.value)
        }
    }

    fun retryGetMovies() {
        moviesRepoResult.value?.retry?.invoke()
    }

    fun refreshGetMovies() {
        moviesRepoResult.value?.refresh?.invoke()
    }

    fun showMoviesOfCategory(category: String): Boolean {
        if (savedStateHandle.get<String>(KEY_MOVIE_CATEGORY) == category) return false
        savedStateHandle.set(KEY_MOVIE_CATEGORY, category)
        return true
    }

    companion object {

        private const val KEY_MOVIE_CATEGORY = "movie_category"

    }

}
