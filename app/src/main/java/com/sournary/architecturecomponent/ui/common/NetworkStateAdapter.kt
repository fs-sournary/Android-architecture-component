package com.sournary.architecturecomponent.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.sournary.architecturecomponent.databinding.ItemNetworkStateBinding
import com.sournary.architecturecomponent.ext.setSafeClick

/**
 * The adapter class handle logic of network footer items of a Paging RecyclerView
 * Maybe action: retry, refresh
 * Maybe state: loading, done...
 */
class NetworkStateAdapter(private val retry: (() -> Unit)) :
    LoadStateAdapter<ListViewHolder<ItemNetworkStateBinding>>() {

    override fun onBindViewHolder(
        holder: ListViewHolder<ItemNetworkStateBinding>, loadState: LoadState
    ) {
        holder.binding.apply {
            isLoading = loadState == LoadState.Loading
            if (loadState is LoadState.Error) {
                message = loadState.error.localizedMessage
            }
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): ListViewHolder<ItemNetworkStateBinding> {
        val binding =
            ItemNetworkStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.retryButton.setSafeClick(View.OnClickListener { retry.invoke() })
        return ListViewHolder(binding)
    }

}
