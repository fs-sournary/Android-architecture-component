package com.sournary.architecturecomponent.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sournary.architecturecomponent.repository.MovieRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * The factory class of home view model.
 */
class HomeViewModelFactory : ViewModelProvider.NewInstanceFactory(), KoinComponent {

    private val movieRepository: MovieRepository by inject()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        HomeViewModel(movieRepository) as T

}
