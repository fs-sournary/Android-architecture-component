package com.sournary.architecturecomponent.data

import com.google.gson.annotations.SerializedName

/**
 * A list of movie that is received from server.
 */
data class MovieListResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<Movie>? = null,
    @SerializedName("total_pages")
    val totalPage: Int? = null,
    @SerializedName("total_results")
    val totalResult: Int? = null
)
