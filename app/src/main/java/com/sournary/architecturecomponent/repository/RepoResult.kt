package com.sournary.architecturecomponent.repository

/**
 * The wrapper class is the result that is return by repository's remote method.
 * It contains data and other state of data such as loading, success, error...
 *
 * @param T the data that is bound when remote method is success.
 */
data class RepoResult<T>(
    val data: T? = null,
    val networkState: NetworkState? = null,
    val refreshState: NetworkState? = null
)
