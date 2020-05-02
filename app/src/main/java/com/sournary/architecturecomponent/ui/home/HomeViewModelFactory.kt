package com.sournary.architecturecomponent.ui.home

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.sournary.architecturecomponent.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * The factory class of home view model.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModelFactory(owner: SavedStateRegistryOwner) :
    AbstractSavedStateViewModelFactory(owner, null), KoinComponent {

    private val movieRepository: MovieRepository by inject()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = HomeViewModel(movieRepository, handle) as T

}
