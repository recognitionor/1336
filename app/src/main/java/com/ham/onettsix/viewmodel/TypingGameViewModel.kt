package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.TypingGameList
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TypingGameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val typingGame = MutableLiveData<Resource<TypingGameList>>()

    val typingGameTimer = MutableLiveData<Float>()

    val readyCountDown = MutableLiveData(0)

    val typingGameStatus = MutableLiveData(GAME_START_STATUS_DEFAULT)

    // 0-> 게임 시작전, 1 -> 게임중, 2 -> 게임끝
    companion object {
        const val GAME_START_STATUS_DEFAULT = 0
        const val GAME_START_STATUS_READY = 1
        const val GAME_START_STATUS_ING = 2
        const val GAME_START_STATUS_DONE = 3
    }

    fun getGame(gameType: String) {
    }

    fun ready() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            readyCountDown.value = 3
            while ((readyCountDown.value ?: 0) > 0) {
                delay(1000)
                readyCountDown.postValue(readyCountDown.value?.minus(1))
            }
            typingGameStatus.postValue(GAME_START_STATUS_READY)
        }
    }

    fun startGame() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "e $e")
        }
        typingGameStatus.value = GAME_START_STATUS_ING
        viewModelScope.launch(exceptionHandler) {
            val timeOffset = System.currentTimeMillis()
            while (typingGameStatus.value == GAME_START_STATUS_ING) {
                val time = (System.currentTimeMillis() - timeOffset).toFloat() / 1000
                typingGameTimer.postValue(time)
                delay(100)
            }
        }
    }

    fun finishGame() {
        typingGameStatus.value = GAME_START_STATUS_DONE
    }

}
