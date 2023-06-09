package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.EpisodeList
import com.ham.onettsix.data.model.GameTypeInfo
import com.ham.onettsix.data.model.LotteryInfo
import com.ham.onettsix.data.model.NewNotice
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.*

class HomeViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    companion object {
        const val GAME_TYPE_RPC = "RPC"
    }

    val lotteryInfo = MutableLiveData<Resource<LotteryInfo>>()

    private val newNotice = MutableLiveData<Resource<NewNotice>>()

    val episodeList = MutableLiveData<Resource<EpisodeList>>()

    val winningViewModel = MutableLiveData<Resource<Result>>()

    val gameTypeInfo = MutableLiveData<Resource<GameTypeInfo>>()

    fun getGameTypeInfo() {
        gameTypeInfo.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            gameTypeInfo.postValue(Resource.error("", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val params = HashMap<String, Any?>().apply {
                    this[ParamsKeys.KEY_GAME_TYPE] = RPSGameViewModel.GAME_TYPE_RPC
                }
                val result = apiHelper.getGameCount(params)
                gameTypeInfo.postValue(Resource.success(result))
            }
        }
    }

    fun getInstanceLottery(remainTicket: Int) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            winningViewModel.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val param = HashMap<String, Any?>()
                param[ParamsKeys.KEY_LOTTERY_COUNT] = remainTicket
                val result = apiHelper.getInstantLottery(param)
                winningViewModel.postValue(Resource.success(result))
            }
        }
    }

    fun isLogin(): Boolean {
        var isLogin = false
        CoroutineScope(Dispatchers.Main).launch {
            isLogin = async(Dispatchers.Default) {
                return@async (dbHelper.getUser()?.uid ?: -1) > 0
            }.await()
        }
        return isLogin
    }

    fun getLotteryInfo() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            lotteryInfo.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getLotteryInfo("ALL")
                lotteryInfo.postValue(Resource.success(result))
            }
        }
    }

    fun getEpisodeList() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            lotteryInfo.postValue(Resource.error("error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getEpisodeList()
                episodeList.postValue(Resource.success(result))
            }
        }
    }

    fun getNewNotice() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            newNotice.postValue(Resource.error("newNotice error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getNewNotice()
                newNotice.postValue(Resource.success(result))
            }
        }
    }
}
