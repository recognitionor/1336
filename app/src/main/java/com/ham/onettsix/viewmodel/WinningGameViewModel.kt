package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WinningGameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val winningViewModel = MutableLiveData<Resource<Result>>()

    fun validateLimitedRv() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            winningViewModel.postValue(Resource.error("", null))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val result = apiHelper.getInstantLottery()
                winningViewModel.postValue(Resource.success(result))
            }
        }
    }
}
