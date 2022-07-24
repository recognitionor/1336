package com.ham.onettsix.data.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper private constructor(ctx: Context, val name: String) {

    companion object {

        private var instance: PreferencesHelper? = null

        fun getInstance(ctx: Context): PreferencesHelper {
            if (instance == null) {
                instance = PreferencesHelper(ctx, ctx.packageName)
            }
            return instance!!
        }
    }

    private val pref: SharedPreferences = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE)

}

