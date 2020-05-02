package com.sournary.architecturecomponent.ui.moviedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.databinding.ItemRelatedMovieBinding
import com.sournary.architecturecomponent.ui.common.BasePagingListAdapter
import com.sournary.architecturecomponent.ui.common.RetryListener

/**
 * The class is the related movies RecyclerView's adapter.
 */
class RelatedMovieAdapter(private val retryListener: RetryListener) :
    BasePagingListAdapter<Movie, ItemRelatedMovieBinding>(Movie.COMPARATOR, retryListener) {

    override fun createBinding(parent: ViewGroup): ItemRelatedMovieBinding =
        ItemRelatedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

}
