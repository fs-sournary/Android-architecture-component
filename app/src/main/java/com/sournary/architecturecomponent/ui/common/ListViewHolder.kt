package com.sournary.architecturecomponent.ui.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Each layout's item should include a "item" variable to bind data.
 */
class ListViewHolder<V : ViewDataBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)
