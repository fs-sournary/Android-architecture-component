package com.sournary.architecturecomponent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.sournary.architecturecomponent.data.Movie
import com.sournary.architecturecomponent.repository.DataState
import com.sournary.architecturecomponent.repository.MovieRepository

/**
 * The view model contains all logic of home screen.
 */
class HomeViewModel(movieRepository: MovieRepository) : ViewModel() {

    private val nowPlayingRepoResult = movieRepository.getNowPlayingMovies(viewModelScope)
    val nowPlayingMovies: LiveData<PagedList<Movie>> = nowPlayingRepoResult.data
    val networkState: LiveData<DataState> = nowPlayingRepoResult.dataState!!
    val refreshState: LiveData<DataState> = nowPlayingRepoResult.refreshState!!

    fun refreshNowPlayingMovies() {
        nowPlayingRepoResult.refresh?.invoke()
    }

    fun retryNowPlayingMovies() {
        nowPlayingRepoResult.retry?.invoke()
    }

}
