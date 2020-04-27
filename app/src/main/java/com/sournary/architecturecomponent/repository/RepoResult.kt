package com.sournary.architecturecomponent.repository

import androidx.lifecycle.LiveData

/**
 * The data class is result in repository's methods and used by any ViewModel.
 */
data class RepoResult<T>(
    val data: LiveData<T>,
    val dataState: LiveData<DataState>? = null,
    val refreshState: LiveData<DataState>? = null,
    val refresh: (() -> Unit)? = null,
    val retry: (() -> Unit)? = null
)
