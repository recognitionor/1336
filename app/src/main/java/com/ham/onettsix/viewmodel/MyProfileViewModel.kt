package com.ham.onettsix.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MyProfileViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val userInfo = MutableLiveData<Resource<DBUser>>()

    fun getUserInfo() {
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
