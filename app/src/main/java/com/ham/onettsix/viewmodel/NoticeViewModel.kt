package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.Notice
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val notice = MutableLiveData<Resource<Notice>>()

    fun getNoticeList() {
        Log.d("jhlee", "getNoticeList : ");
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "e : ${e.message}");
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val test = apiHelper.getNoticeList()
                Log.d("jhlee", "test : $test")
                notice.postValue(Resource.success(apiHelper.getNoticeList()))
            }
        }
    }

}
