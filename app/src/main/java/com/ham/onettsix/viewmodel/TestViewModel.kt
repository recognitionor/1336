package com.ham.onettsix.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.entity.DBTest
import com.ham.onettsix.data.model.Test
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


class TestViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val test = MutableLiveData<Resource<Test>>()

    fun getTestWithHttp() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            test.postValue(Resource.error(e.toString(), null))
        }
        viewModelScope.launch(exceptionHandler) {
            val params = hashMapOf<String, Any?>().apply {
                put(ParamsKeys.KEY_TEST, "token")
            }
            apiHelper.getTest(params)
        }
    }

    fun getTestWithDB(param: DBTest) {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            test.postValue(Resource.error(e.toString(), null))
        }
        viewModelScope.launch(exceptionHandler) {
            dbHelper.deleteTest(param)
        }
    }
}