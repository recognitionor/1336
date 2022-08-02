package com.ham.onettsix.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.viewmodel.SplashViewModel
import com.ham.onettsix.viewmodel.TestViewModel

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
        throw IllegalArgumentException("Unknown class name")
    }
}