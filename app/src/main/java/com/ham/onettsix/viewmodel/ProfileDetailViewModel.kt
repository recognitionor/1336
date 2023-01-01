package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys.KEY_ENABLE_ALARM
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.HistoryInfo
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileDetailViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val userInfo = MutableLiveData<Resource<DBUser>>()

    val withDraw = MutableLiveData<Resource<com.ham.onettsix.data.model.Result>>()

    val changeAlarmResult = MutableLiveData<Resource<com.ham.onettsix.data.model.Result>>()

    private val historyInfo = MutableLiveData<Resource<HistoryInfo>>()

    fun logout() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            userInfo.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                dbHelper.deleteUser()
                preferenceHelper?.removeLogin()
                userInfo.postValue(Resource.success(null))
            }
        }
    }

    fun getUserInfo() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            userInfo.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val tempUserInfo = dbHelper.getUser()
                if (tempUserInfo?.uid != null && tempUserInfo.uid > 0) {
                    userInfo.postValue(Resource.success(tempUserInfo))
                } else {
                    throw Exception("not login")
                }
            }
        }
    }

    fun withDraw() {
        Log.d("jhlee", " withDraw")
        historyInfo.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "error : ${e.message}")
            historyInfo.postValue(Resource.error("signin error", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.withDraw()
                dbHelper.deleteUser()
                preferenceHelper?.removeLogin()
                withDraw.postValue(Resource.success(result))
            }
        }
    }

    fun changeAlarm(isChecked: Boolean) {
        changeAlarmResult.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            changeAlarmResult.postValue(Resource.error("signin error", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val param = HashMap<String, Any?>()
                param[KEY_ENABLE_ALARM] = isChecked
                apiHelper.changeAlarm(param)
            }
        }
    }

    fun getHistoryInfo() {
        historyInfo.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            historyInfo.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getHistoryInfo()
                historyInfo.postValue(Resource.success(result))
            }
        }
    }
}
