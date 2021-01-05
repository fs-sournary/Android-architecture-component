package com.sournary.architecturecomponent.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sournary.architecturecomponent.databinding.ItemHomeMovieBinding
import com.sournary.architecturecomponent.ext.setSafeClick
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.ui.common.ListViewHolder

/**
 * The adapter of movie recycler view.
 */
class MovieListAdapter(private val click: (Movie) -> Unit) :
    PagingDataAdapter<Movie, ListViewHolder<ItemHomeMovieBinding>>(Movie.COMPARATOR) {

    override fun onBindViewHolder(holder: ListViewHolder<ItemHomeMovieBinding>, position: Int) {
        val movie = getItem(position) ?: return
        holder.binding.apply {
            item = movie
            itemMovieRoot.setSafeClick(View.OnClickListener { click.invoke(movie) })
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder<ItemHomeMovieBinding> {
        val binding =
            ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }
}
