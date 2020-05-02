package com.sournary.architecturecomponent.ui.moviedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sournary.architecturecomponent.databinding.ItemRelatedMovieBinding
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.ui.common.ListViewHolder

/**
 * The class is the related movies RecyclerView's adapter.
 */
class RelatedMovieAdapter :
    PagingDataAdapter<Movie, ListViewHolder<ItemRelatedMovieBinding>>(Movie.COMPARATOR) {

    override fun onBindViewHolder(holder: ListViewHolder<ItemRelatedMovieBinding>, position: Int) {
        holder.binding.apply {
            item = getItem(position)
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder<ItemRelatedMovieBinding> {
        val binding =
            ItemRelatedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }
}