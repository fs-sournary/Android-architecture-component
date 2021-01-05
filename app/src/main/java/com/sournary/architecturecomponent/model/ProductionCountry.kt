package com.sournary.architecturecomponent.model

import com.google.gson.annotations.SerializedName

/**
 * The data of country where produced the movie.
 */
class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String? = null,
    @SerializedName("name")
    val name: String? = null
)
