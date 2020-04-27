package com.sournary.architecturecomponent.data

import com.google.gson.annotations.SerializedName

/**
 * The model of genre.
 */
data class Genre(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null
)
