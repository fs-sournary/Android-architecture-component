package com.sournary.architecturecomponent.data

import com.google.gson.annotations.SerializedName

/**
 * The data class represents a list of genres response that is get from server.
 */
data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>? = null
)
