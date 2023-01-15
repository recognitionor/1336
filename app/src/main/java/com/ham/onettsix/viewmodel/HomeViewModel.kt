package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.LotteryInfo
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

    val winningViewModel = MutableLiveData<Resource<Result>>()

    fun getInstanceLottery() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            winningViewModel.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getInstantLottery()
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
}
