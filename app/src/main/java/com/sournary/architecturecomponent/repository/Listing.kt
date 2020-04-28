package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * The data class is result in repository's methods and used by any ViewModel.
 */
data class Listing<T>(
    val data: LiveData<PagedList<T>>,
    val dataState: LiveData<DataState>? = null,
    val refreshState: LiveData<DataState>? = null,
    val refresh: (() -> Unit)? = null,
    val retry: (() -> Unit)? = null
)
