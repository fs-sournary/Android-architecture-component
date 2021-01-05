package com.sournary.architecturecomponent.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.repository.home.HomeRepository
import com.sournary.architecturecomponent.util.Constant.KEY_GENRE

/**
 * The view model contains all logic of home screen.
 */
class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // The Integer stores the last checked id of the chip in the genre_group chip group.
    var checkId = Genre.SAVED_GENRES[0].id

    private val _savedGenre = savedStateHandle.getLiveData<Genre>(KEY_GENRE)
    val savedGenre: LiveData<Genre> = _savedGenre
    val movies = _savedGenre.switchMap { homeRepository.getMovies(it).cachedIn(viewModelScope) }

    private val _getGenres = MutableLiveData<Any>()
    val genres = _getGenres.switchMap { homeRepository.getGenres() }

    init {
        // Save now playing genre into disk for the first time.
        if (!savedStateHandle.contains(KEY_GENRE)) {
            savedStateHandle.set(KEY_GENRE, Genre.SAVED_GENRES[0])
        }
        _getGenres.value = Any()
    }

    fun showGenreMovies(genre: Genre) {
        if (savedStateHandle.get<Genre>(KEY_GENRE) == genre) return
        savedStateHandle.set(KEY_GENRE, genre)
    }

    fun retryGetMovies() {
        val savedGenre = savedStateHandle.get<Genre>(KEY_GENRE) ?: return
        savedStateHandle.set(KEY_GENRE, savedGenre)
        if (genres.value?.size == Genre.SAVED_GENRES.size) {
            checkId = savedGenre.id
            _getGenres.value = Any()
        }
    }
}
