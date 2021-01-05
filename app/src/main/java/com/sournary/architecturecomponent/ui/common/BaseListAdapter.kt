package com.sournary.architecturecomponent.ui.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sournary.architecturecomponent.BR
import java.util.concurrent.Executors

/**
 * Abstract class is the base class for RecyclerView's adapter that contains a list of data.
 *
 * @param T data Object that each instance is used to bind a corresponding layout's item.
 * @param B binding Object of layout's item.
 */
abstract class BaseListAdapter<T, B : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, ListViewHolder<B>>(
        AsyncDifferConfig.Builder<T>(diffCallback)
            .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
            .build()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder<B> {
        val binding = createBinding(parent, viewType)
        return ListViewHolder(binding)
    }

    /**
     * The method creates binding instance of item's layout.
     * @param parent the parent View into which the new view will be added.
     * @param viewType the view type of new view.
     */
    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): B

    override fun onBindViewHolder(holder: ListViewHolder<B>, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.setVariable(BR.item, item)
        bindData(item)
        holder.binding.executePendingBindings()
    }

    /**
     * Beside binding item to layout's item, we can bind or setup other things in layout's item
     * @param item data is bound to layout's item.
     */
    protected open fun bindData(item: T) {}
}
