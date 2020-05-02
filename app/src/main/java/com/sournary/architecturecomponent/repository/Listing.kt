package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * The data class is Paging's result in repository's methods and used by any ViewModel.
 * From paging 3.0, PagedList is unused because it has only private constructor.
 */
data class Listing<T>(
    val data: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>? = null,
    val refreshState: LiveData<NetworkState>? = null,
    val refresh: (() -> Unit)? = null,
    val retry: (() -> Unit)? = null
)
