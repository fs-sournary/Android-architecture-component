package com.sournary.architecturecomponent.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sournary.architecturecomponent.repository.MovieRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * The class represents factory of movie detail view model.
 */
@Suppress("UNCHECKED_CAST")
class MovieDetailViewModelFactory(private val movieId: Int) :
    ViewModelProvider.NewInstanceFactory(), KoinComponent {

    private val movieRepository: MovieRepository by inject()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MovieDetailViewModel(movieId, movieRepository) as T

}
