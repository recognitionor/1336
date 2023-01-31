package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.InvestmentTag
import com.ham.onettsix.data.model.InvestmentInfo
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class InvestmentViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    companion object {
        const val PARAM_KEY_START_PAGE = "startPage"

        const val PARAM_KEY_CONTENT_COUNT = "contentCount"

        const val PARAM_KEY_INVESTMENT_TAG_ID = "investmentTagId"
    }

    val investmentList = MutableLiveData<Resource<InvestmentInfo>>()

    val investmentTagList = MutableLiveData<Resource<InvestmentTag>>()

    fun clearInvestmentList() {
        investmentList.value?.data?.data?.clear()
    }

    fun getInvestmentList(startPage: Int, contentPage: Int, tagId: Int) {
        Log.d("jhlee", "getInvestmentList : $tagId")
        investmentList.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            investmentList.postValue(Resource.error(e.toString(), null))
        }
        viewModelScope.launch(exceptionHandler) {
            val map = HashMap<String, Any?>()
            map[PARAM_KEY_START_PAGE] = startPage
            map[PARAM_KEY_CONTENT_COUNT] = contentPage
            map[PARAM_KEY_INVESTMENT_TAG_ID] = tagId
            val result = apiHelper.getInvestmentList(map)
            Log.d("jhlee", "getInvestmentList result : $result")
            investmentList.postValue(Resource.success(result))
        }
    }

    fun getYoutubeTagList() {
        Log.d("jhlee", "getYoutubeTagList :")
        investmentTagList.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "error : ${e.localizedMessage}")
        }

        viewModelScope.launch(exceptionHandler) {
            viewModelScope.launch {
                val result = apiHelper.getInvestmentTagList()
                investmentTagList.postValue(Resource.success(result))
            }
        }
    }

}
