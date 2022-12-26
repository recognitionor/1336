package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.Test
import com.ham.onettsix.data.model.YouTubeInfo
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class YouTubeViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    companion object {
        const val PARAM_KEY_START_PAGE = "startPage"

        const val PARAM_KEY_CONTENT_COUNT = "contentCount"
    }

    val youtubeList = MutableLiveData<Resource<YouTubeInfo>>()

    fun getYoutubeList(startPage: Int, contentPage: Int) {
        youtubeList.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            youtubeList.postValue(Resource.error("", null))
        }
        Log.d("jhlee", "getYoutubeList : $startPage")
        viewModelScope.launch(exceptionHandler) {
            val map = HashMap<String, Any?>()
            map[PARAM_KEY_START_PAGE] = startPage
            map[PARAM_KEY_CONTENT_COUNT] = contentPage
            val result = apiHelper.getYouTubeList(map)
            youtubeList.postValue(Resource.success(result))
        }
    }

}
