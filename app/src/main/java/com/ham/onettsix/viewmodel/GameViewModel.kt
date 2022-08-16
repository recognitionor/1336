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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    companion object {
        const val GAME_TYPE_RPC = "RPC"
    }

    val result = MutableLiveData<Resource<Result>>()

    val gameTypeInfo = MutableLiveData<Resource<GameTypeInfo>>()

    val gameResult = MutableLiveData<Resource<GameResult>>()

    fun gameLoad() {
        gameTypeInfo.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            gameTypeInfo.postValue(Resource.error("", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                dbHelper.getUser().accessToken?.let {
                    val header = HashMap<String, Any>().apply {
                        this[ParamsKeys.KEY_AUTH_TOKEN] = it
                    }
                    val params = HashMap<String, Any?>().apply {
                        this[ParamsKeys.KEY_GAME_TYPE] = GAME_TYPE_RPC
                    }
                    val result = apiHelper.getGameCount(header, params)
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
                if (dbHelper.getUser().accessToken != null) {
                    val header = HashMap<String, Any>().apply {
                        put(ParamsKeys.KEY_AUTH_TOKEN, dbHelper.getUser().accessToken!!)
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
