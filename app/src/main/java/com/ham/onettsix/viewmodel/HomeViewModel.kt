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
        var test = false
        val job = CoroutineScope(Dispatchers.Main).launch {
            Log.d("jhlee", "1 : ${Thread.currentThread().name}")

            val isLogin = async(Dispatchers.Default) {
                Log.d("jhlee", "2 : ${Thread.currentThread().name} ---- ${dbHelper.getUser()}")
                return@async (dbHelper.getUser()?.uid ?: -1) > 0
            }.await()
            test = isLogin
            Log.d("jhlee", "3 : ${Thread.currentThread().name} ---- $isLogin")
        }
        Log.d("jhlee", "4 : $test")
        return test
    }

    fun getLotteryInfo() {
        Log.d("jhlee", "getLotteryInfo")
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "${e.message}")
            lotteryInfo.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getLotteryInfo("ALL")
                Log.d("jhlee", "result : $result")
                lotteryInfo.postValue(Resource.success(result))
            }
        }
    }
}
