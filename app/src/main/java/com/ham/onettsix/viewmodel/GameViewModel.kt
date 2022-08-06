package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.Pagination
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val result = MutableLiveData<Resource<Result>>()

    fun game() {
        Log.d("jhlee", "game")
        result.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            result.postValue(Resource.error("", Result("", Pagination(), "", "", "")))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                Thread.sleep(3000)
                if (true) {
                    result.postValue(Resource.success(Result("", Pagination(), "", "", "")))
                } else {
                    result.postValue(Resource.error("", Result("", Pagination(), "", "", "")))
                }
            }
        }
    }
}
