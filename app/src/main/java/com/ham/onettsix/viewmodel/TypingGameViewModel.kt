package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.TypingGameEnd
import com.ham.onettsix.data.model.TypingGameList
import com.ham.onettsix.data.model.TypingGameStart
import com.ham.onettsix.utils.Resource
import com.ham.onettsix.viewmodel.TypingNormalViewModel.Companion.KEY_GAME_TYPE_N
import com.ham.onettsix.viewmodel.TypingNormalViewModel.Companion.KEY_GAME_TYPE_R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TypingGameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?,
) : ViewModel() {

    val questionId = MutableLiveData<Int>()

    val episode = MutableLiveData<Int>()

    val content = MutableLiveData<String>()

    val invalidChecker = MutableLiveData<MutableMap<String, Boolean>>()

    private val startTypingGame = MutableLiveData<TypingGameStart.Data>()

    val startTypingGameError = MutableLiveData<String>()

    val endTypingGame = MutableLiveData<TypingGameEnd.Data>()

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
        const val GAME_START_STATUS_ERROR = 4
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

    fun invalidCheck(substring: String?) {
        substring?.forEach {
            if (invalidChecker.value?.contains(it.toString()) == true) {
                invalidChecker.value?.set(it.toString(), true)
            }
        }
    }

    fun startRankGame() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            typingGameStatus.value = GAME_START_STATUS_ERROR
        }
        typingGameStatus.value = GAME_START_STATUS_ING
        viewModelScope.launch(exceptionHandler) {
            val params = HashMap<String, Any?>()
            params[ParamsKeys.KEY_GAME_TYPE] = KEY_GAME_TYPE_R
            params[ParamsKeys.KEY_EPISODE] = episode.value
            params[ParamsKeys.KEY_GAME_EPISODE] = episode.value
            val result = apiHelper.startTypingGame(
                questionId.value?.toLong() ?: 0, params
            )
            if (result.data != null) {
                startTypingGame.value = result.data
                val timeOffset = System.currentTimeMillis()
                while (typingGameStatus.value == GAME_START_STATUS_ING) {
                    val time = (System.currentTimeMillis() - timeOffset).toFloat() / 1000
                    typingGameTimer.postValue(time)
                    delay(100)
                }
            } else {
                // 2001 No Ticket Error
                startTypingGameError.postValue(result.description)
            }
        }
    }

    fun startRandomGame() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        }
        typingGameStatus.value = GAME_START_STATUS_ING
        viewModelScope.launch(exceptionHandler) {
            val params = HashMap<String, Any?>()
            params[ParamsKeys.KEY_GAME_TYPE] = KEY_GAME_TYPE_N
            startTypingGame.value = apiHelper.startTypingGame(
                questionId.value?.toLong() ?: 0, params
            ).data
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
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            val historyId = startTypingGame.value?.historyId
            val timeOffset = typingGameTimer.value ?: 0f
            if (historyId != null) {
                val params = HashMap<String, Any?>()
                params[ParamsKeys.KEY_GAME_DURATION] = (timeOffset * 1000).toLong()
                val result = apiHelper.endTypingGame(historyId, params)
                endTypingGame.postValue(result.data)
            } else {
            }
        }
    }

    fun setTypingGameInfo(questionId: Int, episode: Int, content: String?) {
        this.questionId.postValue(questionId)
        this.episode.postValue(episode)
        this.content.postValue(content)
        val map = mutableMapOf<String, Boolean>()
        content?.forEach {
            map[it.toString()] = false
        }
        this.invalidChecker.postValue(map)
    }
}
