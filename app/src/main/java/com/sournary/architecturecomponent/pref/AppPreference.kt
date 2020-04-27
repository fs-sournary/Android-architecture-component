package com.sournary.architecturecomponent.pref

import android.content.Context
import androidx.core.content.edit

/**
 * The class represents for app's preference.
 */
class AppPreference(context: Context) {

    private val pref =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setAccessToken(token: String) {
        pref.edit { putString(ACCESS_TOKEN_KEY, token) }
    }

    fun getAccessToken(): String =
        pref.getString(ACCESS_TOKEN_KEY, ACCESS_TOKEN_DEF) ?: ACCESS_TOKEN_DEF

    companion object {

        private const val PREF_NAME = "architecture_component_preference"
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val ACCESS_TOKEN_DEF = "access_token"

    }

}
