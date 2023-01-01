package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys.KEY_EPISODE
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.HistoryInfo
import com.ham.onettsix.data.model.WinnerSecretCode
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProfileViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val userInfo = MutableLiveData<Resource<DBUser>>()

    val historyInfo = MutableLiveData<Resource<HistoryInfo>>()

    val winnerSecretCode = MutableLiveData<Resource<WinnerSecretCode>>()

    fun getWinnerSecretCode(episode: Int) {
        Log.d("jhlee", "getWinnerSecretCode")
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "e : ${e.message}")
            winnerSecretCode.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val param = HashMap<String, Any?>()
                param[KEY_EPISODE] = episode
                val result = apiHelper.getSecretCode(param)
                Log.d("jhlee", "getWinnerSecretCode : $result")
                winnerSecretCode.postValue(Resource.success(result))
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

    fun getHistoryInfo() {
        historyInfo.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "error : ${e.message}")
            historyInfo.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getHistoryInfo()
                historyInfo.postValue(Resource.success(result))
                Log.d("jhlee", "result getHistoryInfo : $result")

            }
        }
    }
}
