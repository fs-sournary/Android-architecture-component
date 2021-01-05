package com.sournary.architecturecomponent.data.pref

import android.content.Context
import androidx.core.content.edit

/**
 * The class represents for app's preference.
 */
interface AppPreference {

    fun setAccessToken(token: String)

    fun getAccessToken(): String
}
