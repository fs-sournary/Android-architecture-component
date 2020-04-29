package com.sournary.architecturecomponent.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * The file defines extension functions for ImageView.
 */
@BindingAdapter(
    value = ["localImageId", "centerCrop", "circleCrop"],
    requireAll = false
)
fun ImageView.loadLocalImage(id: Int, centerCrop: Boolean, circleCrop: Boolean) {
    var requestBuilder = Glide.with(context).load(id)
    if (centerCrop) {
        requestBuilder = requestBuilder.centerCrop()
    }
    if (circleCrop) {
        requestBuilder = requestBuilder.centerCrop()
    }
    requestBuilder.into(this)
}

@BindingAdapter(
    value = ["url", "centerCrop", "circleCrop"],
    requireAll = false
)
fun ImageView.loadUrlImage(url: String, centerCrop: Boolean, circleCrop: Boolean) {
    var requestBuilder = Glide.with(context).load(url)
    if (centerCrop) {
        requestBuilder = requestBuilder.centerCrop()
    }
    if (circleCrop) {
        requestBuilder = requestBuilder.circleCrop()
    }
    requestBuilder.into(this)
}
