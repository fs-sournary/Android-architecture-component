package com.sournary.architecturecomponent.repository

/**
 * The file defines status of data.
 */
@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val message: String? = null
) {

    companion object {

        val LOADING = NetworkState(Status.LOADING)
        val SUCCESS = NetworkState(Status.SUCCESS)

        fun error(message: String) = NetworkState(Status.ERROR, message)

    }

}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}
