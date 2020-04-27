package com.sournary.architecturecomponent.ext

import android.os.SystemClock
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

/**
 * The file defines extension functions of View.
 */
private const val MIN_SAFE_CLICK_TIME = 1000

@BindingAdapter("onSafeClick")
fun View.setSafeClick(listener: View.OnClickListener) {
    var lastClickTime = 0L
    setOnClickListener { v ->
        if (SystemClock.elapsedRealtime() - lastClickTime < MIN_SAFE_CLICK_TIME) {
            return@setOnClickListener
        }
        lastClickTime = SystemClock.elapsedRealtime()
        listener.onClick(v)
    }
}

@BindingAdapter("visibleGone")
fun View.setVisibleGone(visible: Boolean) {
    isVisible = visible
}
