package com.sournary.architecturecomponent.ui.home

import androidx.lifecycle.*
import com.sournary.architecturecomponent.data.Genre
import com.sournary.architecturecomponent.repository.MovieRepository

/**
 * The view model contains all logic of home screen.
 */
class HomeViewModel(
    movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val savedGenre = savedStateHandle.getLiveData<Genre>(KEY_GENRE)
    private val moviesRepoResult = savedGenre.map {
        movieRepository.getMovies(viewModelScope, it)
    }
    val movies = moviesRepoResult.switchMap { it.data }
    val dataState = moviesRepoResult.switchMap { it.dataState!! }
    val refreshState = moviesRepoResult.switchMap { it.refreshState!! }

    val genres: LiveData<List<Genre>> = movieRepository.genresUsingSwitchMap

    init {
        // Save now playing genre into disk for the first time.
        if (!savedStateHandle.contains(KEY_GENRE)) {
            savedStateHandle.set(KEY_GENRE, Genre.SAVED_GENRES[0])
        }
    }

    fun retryGetMovies() {
        moviesRepoResult.value?.retry?.invoke()
    }

    fun refreshGetMovies() {
        moviesRepoResult.value?.refresh?.invoke()
    }

    fun showMoviesOfCategory(genre: Genre): Boolean {
        if (savedStateHandle.get<Genre>(KEY_GENRE) == genre) return false
        savedStateHandle.set(KEY_GENRE, genre)
        return true
    }

    companion object {

        private const val KEY_GENRE = "movie_genre"

    }

}
