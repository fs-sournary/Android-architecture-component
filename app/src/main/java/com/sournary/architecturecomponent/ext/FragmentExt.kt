package com.sournary.architecturecomponent.ext

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.sournary.architecturecomponent.util.AutoClearValue

/**
 * The file defines extension functions for app's Fragment.
 */
fun Fragment.hideKeyboard() {
    val ime = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    ime.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun <T : Any> Fragment.autoCleared(): AutoClearValue<T> = AutoClearValue(this)
