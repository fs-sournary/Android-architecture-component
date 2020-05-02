package com.sournary.architecturecomponent.model

import com.google.gson.annotations.SerializedName

/**
 * The data class represents video.
 */
data class Video(
    @SerializedName("id")
    val id: Int,
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("site")
    val site: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("size")
    val size: Int? = null,
    @SerializedName("type")
    val type: String? = null
)
