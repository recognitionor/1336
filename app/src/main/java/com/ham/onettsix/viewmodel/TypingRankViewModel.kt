package com.ham.onettsix.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.TypingGameItem
import com.ham.onettsix.data.model.TypingGameList
import com.ham.onettsix.data.model.TypingGameMyInfo
import com.ham.onettsix.data.model.TypingGameRankMain
import com.ham.onettsix.data.model.TypingGameValidation
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TypingRankViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?,
) : ViewModel() {

    val typingGameList = MutableLiveData<Resource<TypingGameList>>()

    val rankGame = MutableLiveData<Resource<TypingGameRankMain.Data>>()

    val selectedTypingGame = MutableLiveData<Resource<TypingGameItem.Data>>()

    val myTypingGameRecord = MutableLiveData<Resource<List<TypingGameMyInfo.Data>>>()

    val userInfo = MutableLiveData<Resource<DBUser>>()

    val typingGameValidation = MutableLiveData<Resource<TypingGameValidation>>()

    init {
        getRankMain()
        getMyPage()
    }

    fun getMyPage() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                userInfo.postValue(Resource.success(dbHelper.getUser()))
                val result = apiHelper.getMyPage()
                myTypingGameRecord.postValue(Resource.success(result.data))
            }
        }
    }

    fun getTypingGameValidation() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            rankGame.postValue(Resource.error(e.message.toString(), null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val params = apiHelper.getTypeGameValidation()
                typingGameValidation.postValue(Resource.success(params))
            }
        }
    }

    fun getRankMain() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            rankGame.postValue(Resource.error(e.message.toString(), null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                rankGame.postValue(Resource.loading(null))
                val params = hashMapOf<String, Any?>()
                params[ParamsKeys.KEY_GAME_TYPE] = TypingNormalViewModel.KEY_GAME_TYPE_R
                val result = apiHelper.getRankMain()
                rankGame.postValue(Resource.success(result.data))
            }
        }
    }

}
