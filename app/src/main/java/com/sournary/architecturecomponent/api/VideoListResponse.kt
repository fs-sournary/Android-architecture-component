package com.sournary.architecturecomponent.api

import com.google.gson.annotations.SerializedName
import com.sournary.architecturecomponent.model.Video

/**
 * The data class represents a list of video that is fetched from server.
 */
data class VideoListResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("results")
    val results: List<Video>? = null
)
