package com.ham.onettsix.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
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
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val questionId = MutableLiveData<Int>()

    val episode = MutableLiveData<Int>()

    val content = MutableLiveData<String>()

    private val startTypingGame = MutableLiveData<TypingGameStart.Data>()

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

    fun startRankGame() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            typingGameStatus.value = GAME_START_STATUS_ERROR
        }
        viewModelScope.launch(exceptionHandler) {
            val params = HashMap<String, Any?>()
            params[ParamsKeys.KEY_GAME_TYPE] = KEY_GAME_TYPE_R
            params[ParamsKeys.KEY_EPISODE] = episode.value
            startTypingGame.value = apiHelper.startTypingGame(
                questionId.value?.toLong() ?: 0, params
            ).data

            typingGameStatus.value = GAME_START_STATUS_ING
            val timeOffset = System.currentTimeMillis()
            while (typingGameStatus.value == GAME_START_STATUS_ING) {
                val time = (System.currentTimeMillis() - timeOffset).toFloat() / 1000
                typingGameTimer.postValue(time)
                delay(100)
            }
        }
    }

    fun startRandomGame() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            val params = HashMap<String, Any?>()
            params[ParamsKeys.KEY_GAME_TYPE] = KEY_GAME_TYPE_N
            startTypingGame.value = apiHelper.startTypingGame(
                questionId.value?.toLong() ?: 0, params
            ).data
            typingGameStatus.value = GAME_START_STATUS_ING
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
            val timeOffset = typingGameTimer.value
            if (historyId != null && timeOffset != null) {
                val params = HashMap<String, Any?>()
                params[ParamsKeys.KEY_GAME_DURATION] = (timeOffset * 1000).toLong()
                apiHelper.endTypingGame(historyId, params)
            }
        }
        typingGameTimer.value = 0f
    }

    fun setTypingGameInfo(questionId: Int, episode: Int, content: String?) {
        this.questionId.postValue(questionId)
        this.episode.postValue(episode)
        this.content.postValue(content)
    }
}
