package com.ham.onettsix.data.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper private constructor(ctx: Context, val name: String) {

    companion object {
        const val KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN"
        const val KEY_USER_ID = "KEY_USER_ID"

        private var instance: PreferencesHelper? = null

        fun getInstance(ctx: Context): PreferencesHelper {
            if (instance == null) {
                instance = PreferencesHelper(ctx, ctx.packageName)
            }
            return instance!!
        }
    }

    private val pref: SharedPreferences = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE)

    private val saver: SharedPreferences.Editor = pref.edit()

    fun setFireBaseToken(token: String) {
        saver.putString(KEY_FIREBASE_TOKEN, token)
        saver.commit()
    }

    fun getFireBaseToken(): String {
        return pref.getString(KEY_FIREBASE_TOKEN, "") ?: ""
    }

    fun setLogin(uid: Int) {
        saver.putInt(KEY_USER_ID, uid)
        saver.commit()
    }

    fun removeLogin() {
        saver.remove(KEY_USER_ID)
        saver.commit()
    }

    fun isLogin(): Boolean {
        return pref.getInt(KEY_USER_ID, -1) > 0
    }
}

