package com.sournary.architecturecomponent.repository.home

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.repository.result.RepoResult

interface HomeRepository {

    fun getGenres(): LiveData<List<Genre>>

    fun getMovieDetail(id: Int): LiveData<RepoResult<Movie>>

    fun getMovies(genre: Genre): LiveData<PagingData<Movie>>
}
