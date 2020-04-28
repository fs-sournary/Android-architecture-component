package com.sournary.architecturecomponent.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sournary.architecturecomponent.data.Movie
import com.sournary.architecturecomponent.repository.MovieRepository

/**
 * The view model class contains all logic of movie detail screen.
 */
class MovieDetailViewModel(movieId: Int, movieRepository: MovieRepository) : ViewModel() {

    val movie: LiveData<Movie> = movieRepository.getMovieDetail(movieId)

}
