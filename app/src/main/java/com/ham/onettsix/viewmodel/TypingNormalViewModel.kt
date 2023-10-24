package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.TypingGameItem
import com.ham.onettsix.data.model.TypingGameList
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TypingNormalViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val typingGameList = MutableLiveData<Resource<TypingGameList>>()

    val selectedTypingGame = MutableLiveData<Resource<TypingGameItem.Data>>()

    companion object {
        const val KEY_GAME_TYPE_R = "R"
        const val KEY_GAME_TYPE_N = "N"
    }

    init {
        getTypingGameList()
    }

    fun getTypingGame(itemId: Long) {
        Log.d("jhlee", "getTypingGame : $itemId")
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "e : ${e.message}")
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val params = hashMapOf<String, Any?>()
                params[ParamsKeys.KEY_GAME_QUESTION_ID] = itemId
                val result = apiHelper.getTypingGame(params)
                Log.d("jhlee", "getTypingGame result : $result")
                selectedTypingGame.postValue(Resource.success(result))
            }
        }
    }

    private fun getTypingGameList() {
        Log.d("jhlee", "getTypingGame")
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "e : ${e.message}")
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val params = hashMapOf<String, Any?>()
                params[ParamsKeys.KEY_GAME_TYPE] = KEY_GAME_TYPE_R
                val result = apiHelper.getTypingGameList(params)
                typingGameList.postValue(Resource.success(result))
            }
        }
    }

}
