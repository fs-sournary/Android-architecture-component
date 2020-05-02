package com.sournary.architecturecomponent.repository

/**
 *
 */
data class RepoResult<T>(
    val data: T? = null,
    val networkState: NetworkState? = null,
    val refreshState: NetworkState? = null
)
