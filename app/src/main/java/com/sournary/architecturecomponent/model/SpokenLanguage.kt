package com.sournary.architecturecomponent.model

import com.google.gson.annotations.SerializedName

/**
 * The data of language that is spoken in the movie.
 */
data class SpokenLanguage(
    @SerializedName("iso_639_1")
    val iso6391: String? = null,
    @SerializedName("name")
    val name: String? = null
)
