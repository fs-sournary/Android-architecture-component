package com.sournary.architecturecomponent.data.pref

import android.content.Context
import androidx.core.content.edit
import com.sournary.architecturecomponent.util.Constant.ACCESS_TOKEN_DEF
import com.sournary.architecturecomponent.util.Constant.ACCESS_TOKEN_KEY
import com.sournary.architecturecomponent.util.Constant.PREF_NAME

class DefaultAppPreference(context: Context) : AppPreference {

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun setAccessToken(token: String) {
        pref.edit { putString(ACCESS_TOKEN_KEY, token) }
    }

    override fun getAccessToken(): String =
        pref.getString(ACCESS_TOKEN_KEY, ACCESS_TOKEN_DEF) ?: ACCESS_TOKEN_DEF
}