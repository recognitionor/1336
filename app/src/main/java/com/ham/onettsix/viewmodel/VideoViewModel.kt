package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.RVConfig
import com.ham.onettsix.data.model.RvConfigList
import com.ham.onettsix.data.model.ValidVideoLimitedRv
import com.ham.onettsix.data.model.VideoSignature
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val validateLimitedRvStatus = MutableLiveData<Resource<ValidVideoLimitedRv>>()

    val videoSignature = MutableLiveData<Resource<VideoSignature>>()

    val typingRvConfig = MutableLiveData<Resource<RvConfigList>>()

    fun validateLimitedRv() {
        validateLimitedRvStatus.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            validateLimitedRvStatus.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.validateLimitedRv()
                validateLimitedRvStatus.postValue(Resource.success(result))
            }
        }
    }

    fun getRvConfig() {
        typingRvConfig.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            typingRvConfig.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val param = HashMap<String, Any?>()
                param[ParamsKeys.KEY_AD_UNIT_ID] = "typing"
                val result = apiHelper.getRvConfig(param)
                typingRvConfig.postValue(Resource.success(result))
            }
        }
    }

    fun getVideoSignature() {
        videoSignature.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            videoSignature.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getSignature()
                videoSignature.postValue(Resource.success(result))
            }
        }
    }
}
