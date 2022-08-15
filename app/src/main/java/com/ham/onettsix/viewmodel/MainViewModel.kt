package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys.KEY_ACCESS_TOKEN
import com.ham.onettsix.data.api.ParamsKeys.KEY_AUTH_TOKEN
import com.ham.onettsix.data.api.ParamsKeys.KEY_ENABLE_ALARM
import com.ham.onettsix.data.api.ParamsKeys.KEY_ENABLE_ALERT
import com.ham.onettsix.data.api.ParamsKeys.KEY_SOCIAL_TYPE
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.SignIn
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.*
import okhttp3.internal.wait
import java.lang.Exception

class MainViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val userInfo = MutableLiveData<Resource<DBUser>>()


    fun isLogin(): Boolean {
        return (userInfo.value?.data?.uid ?: -1) > 0
    }

    fun updateUserInfo() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            userInfo.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val tempUserInfo = dbHelper.getUser()
                if (tempUserInfo.uid != null && tempUserInfo.uid > 0) {
                    userInfo.postValue(Resource.success(tempUserInfo))
                } else {
                    throw Exception("not login")
                }
            }
        }
    }


}
