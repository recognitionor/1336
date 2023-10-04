package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.Pagination
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.data.model.TypingGame
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TypingGameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val typingGame = MutableLiveData<Resource<TypingGame>>()

    val typingGameTimer = MutableLiveData<Float>()

    val typingGameStatus = MutableLiveData(false)

    fun getGame() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "e $e")
        }
        viewModelScope.launch(exceptionHandler) {
            Log.d("jhlee", "getGame :")
            val map = hashMapOf<String, Any?>()
            val result = apiHelper.getTypingGame(map)
            typingGame.postValue(Resource.success(result))
        }
    }

    fun startGame() {
        Log.d("jhlee", "startGame")
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "e $e")
        }
        viewModelScope.launch(exceptionHandler) {
            Log.d("jhlee", "launch :")
            val timeOffset = System.currentTimeMillis()
            while (typingGameStatus.value == false) {
                val time = (System.currentTimeMillis() - timeOffset).toFloat() / 1000

                typingGameTimer.postValue(time)
                delay(100)
            }
        }
    }

}
