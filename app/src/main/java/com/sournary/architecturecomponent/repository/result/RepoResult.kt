package com.sournary.architecturecomponent.repository.result

/**
 * A wrapper class for repositories's data and also handle its loading states: success, loading and
 * error
 */
sealed class RepoResult<out R> {

    object Loading : RepoResult<Nothing>()

    data class Success<out T>(val data: T) : RepoResult<T>()

    data class Error(val exception: Exception) : RepoResult<Nothing>()

    override fun toString(): String = when (this) {
        is Loading -> "Loading"
        is Success -> "Success[data=$data]"
        is Error -> "Error[exception=$exception]"
    }
}

/**
 * Return data if it is [RepoResult.Success]. Otherwise, we return null.
 */
val <T> RepoResult<T>.data: T?
    get() = (this as? RepoResult.Success)?.data

val <T> RepoResult<T>.error: Exception?
    get() = (this as? RepoResult.Error)?.exception
