package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.data.model.TypingGameTag
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TypingGameRegisterViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val tagList = MutableLiveData<Resource<List<TypingGameTag.Data>>>()

    val registerResult = MutableLiveData<Resource<Boolean>>()


    init {
        getTagList()
    }

    fun registerTypingGame(content: String, description: String, selectedList: List<String>) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        }
        viewModelScope.launch(exceptionHandler) {
            registerResult.postValue(Resource.loading(null))
            val param = HashMap<String, Any?>()
            param[ParamsKeys.KEY_ACCESS_CONTENT] = content
            param[ParamsKeys.KEY_TAGS] =
                selectedList.joinToString(prefix = "", postfix = "", separator = ",")
            param[ParamsKeys.KEY_QUESTION_EXPLAIN] = description
            val result = apiHelper.registerTypingGame(param)
            if (result.resultCode == ResultCode.REGISTER_SUCCESS) {
                registerResult.postValue(Resource.success(true))
            } else {
                registerResult.postValue(Resource.error(result.description, false))
            }
        }
    }

    fun getTagList() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getTagList().data
                Log.d("jhlee", "tagList : $result")
                tagList.postValue(Resource.success(result))
            }
        }

    }
}
