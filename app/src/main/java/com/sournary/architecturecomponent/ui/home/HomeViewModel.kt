package com.sournary.architecturecomponent.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * The view model contains all logic of home screen.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel(
    movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _savedGenre = savedStateHandle.getLiveData<Genre>(KEY_GENRE)
    val savedGenre: LiveData<Genre>
        get() = _savedGenre
    val movies = _savedGenre.switchMap { movieRepository.getMovies(it).cachedIn(viewModelScope) }

    val genres: LiveData<List<Genre>> = movieRepository.genres

    init {
        // Save now playing genre into disk for the first time.
        if (!savedStateHandle.contains(KEY_GENRE)) {
            savedStateHandle.set(KEY_GENRE, Genre.SAVED_GENRES[0])
        }
    }

    fun showMoviesOfCategory(genre: Genre) {
        if (savedStateHandle.get<Genre>(KEY_GENRE) == genre) return
        savedStateHandle.set(KEY_GENRE, genre)
    }

    fun retryGetMovies(){
        val savedGenre = savedStateHandle.get<Genre>(KEY_GENRE)
        savedStateHandle.set(KEY_GENRE, savedGenre)
    }

    companion object {

        private const val KEY_GENRE = "movie_genre"

    }

}
