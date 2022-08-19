package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.GameResult
import com.ham.onettsix.data.model.GameTypeInfo
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.*

class GameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    companion object {
        const val GAME_TYPE_RPC = "RPC"
    }

    val result = MutableLiveData<Resource<Result>>()

    val attendStatus = MutableLiveData<Resource<Result>>()

    val validateLimitedRvStatus = MutableLiveData<Resource<Result>>()

    val videoSignature = MutableLiveData<Resource<Result>>()

    val gameTypeInfo = MutableLiveData<Resource<GameTypeInfo>>()

    val gameResult = MutableLiveData<Resource<GameResult>>()

    fun validateLimitedRv() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            videoSignature.postValue(Resource.error("", null))
            Log.d("jhlee", "validateLimitedRv Error: ${e.message}")
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.validateLimitedRv()
                Log.d("jhlee", "validateLimitedRv : $result")
            }
        }
    }

    fun getVideoSignature() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            videoSignature.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getSignature()
                Log.d("jhlee", "video Result $result")
            }
        }
    }

    fun attendCheck() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            attendStatus.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.validateAttendCheck()
                Log.d("jhlee", "validateAttendCheck $result")
            }
        }
    }

    fun attendLoad() {
        attendStatus.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            attendStatus.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.attendCheck()
                Log.d("jhlee", "attendLoad result $result")
                delay(3000)
                attendStatus.postValue(Resource.success(result))

            }
        }
    }

    fun gameLoad() {
        gameTypeInfo.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            gameTypeInfo.postValue(Resource.error("", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                dbHelper.getUser()?.accessToken?.let {
                    val header = HashMap<String, Any>().apply {
                        this[ParamsKeys.KEY_AUTH_TOKEN] = it
                    }
                    val params = HashMap<String, Any?>().apply {
                        this[ParamsKeys.KEY_GAME_TYPE] = GAME_TYPE_RPC
                    }
                    val result = apiHelper.getGameCount(header, params)
                    Log.d("jhlee", "gameLoad : $result")
                    gameTypeInfo.postValue(Resource.success(result))
                }
            }
        }
    }

    fun getRockPaperScissors() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            gameResult.postValue(Resource.error("", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                if (dbHelper.getUser()?.accessToken != null) {
                    val header = HashMap<String, Any>().apply {
                        put(ParamsKeys.KEY_AUTH_TOKEN, dbHelper.getUser()?.accessToken!!)
                    }
                    val result = apiHelper.getRockPaperScissors(header)
                    gameResult.postValue(Resource.success(result))
                } else {
                    throw Exception("not login")
                }
            }
        }
    }
}
