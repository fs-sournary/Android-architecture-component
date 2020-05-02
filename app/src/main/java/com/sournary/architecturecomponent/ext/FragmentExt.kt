package com.sournary.architecturecomponent.ext

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * The file defines extension functions for app's Fragment.
 */
fun Fragment.hideKeyboard() {
    val ime = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    ime.hideSoftInputFromWindow(view?.windowToken, 0)
}
