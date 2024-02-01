package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.TypingRandomGame
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

    val randomGame = MutableLiveData<Resource<TypingRandomGame>>()

    val selectedTypingGame = MutableLiveData<Resource<TypingGameItem.Data>>()

    companion object {
        const val KEY_GAME_TYPE_R = "R"
        const val KEY_GAME_TYPE_N = "N"
    }

    init {
        getTypingGameList()
    }

    fun getRandomTypingGame() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            val map: HashMap<String, Any?> = HashMap<String, Any?>().apply {
                this[ParamsKeys.KEY_GAME_TYPE] = KEY_GAME_TYPE_N
            }
            val result = apiHelper.getRandomTypingGame(map)
            randomGame.postValue(Resource.success(result))
        }
    }

    fun getTypingGame(itemId: Long) {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val params = hashMapOf<String, Any?>()
                params[ParamsKeys.KEY_GAME_QUESTION_ID] = itemId
                val result = apiHelper.getTypingGame(params)
                selectedTypingGame.postValue(Resource.success(result))
            }
        }
    }

    fun getTypingGameList() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val params = hashMapOf<String, Any?>()
                params[ParamsKeys.KEY_GAME_TYPE] = KEY_GAME_TYPE_N
                val result = apiHelper.getTypingGameList(params)
                typingGameList.postValue(Resource.success(result))
            }
        }
    }

}
