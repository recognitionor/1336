package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.GameResult
import com.ham.onettsix.data.model.GameTypeInfo
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RPSGameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    companion object {
        const val GAME_TYPE_RPC = "RPC"
    }

    val gameTypeInfo = MutableLiveData<Resource<GameTypeInfo>>()

    val gameResult = MutableLiveData<Resource<GameResult>>()

    fun gameLoad() {
        gameTypeInfo.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            gameTypeInfo.postValue(Resource.error("", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val params = HashMap<String, Any?>().apply {
                    this[ParamsKeys.KEY_GAME_TYPE] = GAME_TYPE_RPC
                }
                val result = apiHelper.getGameCount(params)
                gameTypeInfo.postValue(Resource.success(result))
            }
        }
    }

    fun getRockPaperScissors() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            gameResult.postValue(Resource.error("", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getRockPaperScissors()
                gameResult.postValue(Resource.success(result))
            }
        }
    }
}
