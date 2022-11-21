package com.ham.onettsix.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.GameResult
import com.ham.onettsix.data.model.GameTypeInfo
import com.ham.onettsix.data.model.LotteryInfo
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            winningViewModel.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getInstantLottery()
                winningViewModel.postValue(Resource.success(result))
            }
        }
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
