package com.sournary.architecturecomponent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sournary.architecturecomponent.data.Movie
import com.sournary.architecturecomponent.databinding.ItemHomeMovieBinding
import com.sournary.architecturecomponent.ui.common.BasePagingListAdapter
import com.sournary.architecturecomponent.ui.common.RetryListener

/**
 * The adapter of movie recycler view.
 */
class MovieListAdapter(retryListener: RetryListener) :
    BasePagingListAdapter<Movie, ItemHomeMovieBinding>(Movie.COMPARATOR, retryListener) {

    override fun createBinding(parent: ViewGroup): ItemHomeMovieBinding =
        ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

}
