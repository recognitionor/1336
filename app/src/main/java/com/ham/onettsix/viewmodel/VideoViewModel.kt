package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.data.model.ValidVideoLimitedRv
import com.ham.onettsix.data.model.VideoSignature
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.*

class VideoViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val validateLimitedRvStatus = MutableLiveData<Resource<ValidVideoLimitedRv>>()

    val videoSignature = MutableLiveData<Resource<VideoSignature>>()

    fun validateLimitedRv() {
        Log.d("jhlee", "validateLimitedRv")
        validateLimitedRvStatus.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            validateLimitedRvStatus.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.validateLimitedRv()
                Log.d("jhlee", "validateLimitedRv result : $result")
                validateLimitedRvStatus.postValue(Resource.success(result))
            }
        }
    }

    fun getVideoSignature() {
        Log.d("jhlee", "getVideoSignature")
        videoSignature.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            videoSignature.postValue(Resource.error("", null))
            Log.d("jhlee", "getVideoSignature e: ${e.message}")
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getSignature()
                videoSignature.postValue(Resource.success(result))
            }
        }
    }

    fun addVideoCount() {
        Log.d("jhlee", "getVideoSignature")
    }
}
