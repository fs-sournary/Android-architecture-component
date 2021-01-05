package com.sournary.architecturecomponent.data.api

import com.google.gson.annotations.SerializedName
import com.sournary.architecturecomponent.model.Genre

/**
 * The data class represents a list of genres response that is get from server.
 */
data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>? = null
)
