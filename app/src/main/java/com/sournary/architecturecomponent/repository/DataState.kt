package com.sournary.architecturecomponent.repository

/**
 * The file defines status of data.
 */
@Suppress("DataClassPrivateConstructor")
data class DataState private constructor(
    val status: Status,
    val message: String? = null
) {

    companion object {

        val LOADING = DataState(Status.LOADING)
        val SUCCESS = DataState(Status.SUCCESS)

        fun error(message: String) = DataState(Status.ERROR, message)

    }

}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}
