package com.ham.onettsix.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.viewmodel.*

class ViewModelFactory(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper? = null
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            return TestViewModel(apiHelper, dbHelper) as T
        }
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(apiHelper, dbHelper, preferenceHelper) as T
        }
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(apiHelper, dbHelper, preferenceHelper) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(apiHelper, dbHelper, preferenceHelper) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(apiHelper, dbHelper, preferenceHelper) as T
        }

        if (modelClass.isAssignableFrom(MyProfileViewModel::class.java)) {
            return MyProfileViewModel(apiHelper, dbHelper, preferenceHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}