package com.sournary.architecturecomponent.ext

import androidx.fragment.app.Fragment
import com.sournary.architecturecomponent.util.AutoClearValue

/**
 * The file defines extension functions for app's Fragment.
 */
fun <T : Any> Fragment.autoCleared(): AutoClearValue<T> = AutoClearValue(this)
