package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys.KEY_ACCESS_TOKEN
import com.ham.onettsix.data.api.ParamsKeys.KEY_ENABLE_ALARM
import com.ham.onettsix.data.api.ParamsKeys.KEY_SOCIAL_TYPE
import com.ham.onettsix.data.api.ParamsKeys.KEY_START_EPISODE
import com.ham.onettsix.data.api.ParamsKeys.KEY_TYPE
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.LotteryHistory
import com.ham.onettsix.data.model.SignIn
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LotteryHistoryViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val lotteryHistoryList = MutableLiveData<Resource<ArrayList<LotteryHistory.Data>>>()

    fun getLotteryHistoryList(socialType: String, startEpisode: Int) {
        Log.d("jhlee", "getLotteryHistoryList : ")
        lotteryHistoryList.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            lotteryHistoryList.postValue(Resource.error("signin error", null))
            Log.d("jhlee", "exceptionHandler : ${e.message}")
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val hashMap = HashMap<String, Any?>()
                hashMap[KEY_TYPE] = socialType
                hashMap[KEY_START_EPISODE] = startEpisode
                val result = apiHelper.getLotteryHistory(hashMap)
                lotteryHistoryList.postValue(Resource.success(result.data))
            }
        }
    }
}
