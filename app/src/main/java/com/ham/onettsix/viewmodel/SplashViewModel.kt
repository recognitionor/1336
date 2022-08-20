package com.ham.onettsix.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.StringUtil
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.Pagination
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.*

class SplashViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val result = MutableLiveData<Resource<Result>>()

    fun refreshLogin() {
        result.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            result.postValue(Resource.error("", Result("", Pagination(), -1, "", "")))
            Log.d("jhlee", "e :${e}")
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                delay(1000)
                val user = dbHelper.getUser()
                Log.d("jhlee", "user :${user}")
                val accessToken = user?.accessToken ?: ""
                val refreshToken = user?.refreshToken ?: ""
                Log.d("jhlee", "accessToken :${accessToken}")
                if (user != null &&
                    !TextUtils.isEmpty(accessToken) &&
                    !TextUtils.isEmpty(refreshToken)
                ) {
                    RetrofitBuilder.accessToken = accessToken
                    Log.d("jhlee", "RetrofitBuilder.accessToken :${RetrofitBuilder.accessToken}")
                    val params = HashMap<String, Any>().apply {
                        this[ParamsKeys.KEY_REFRESH_TOKEN] = refreshToken
                    }
                    val result = apiHelper.refreshAccessToken(params)
                    Log.d("jhlee", "refreshAccessToken :${result}")
                    if (result.data != null) {
                        // refresh 를 했는데 데이터가 널이 아닌 경우 정상이므로 토큰값 업데이트 해준다.
                        dbHelper.updateUser(
                            DBUser(
                                result.data.accessToken,
                                result.data.refreshToken,
                                user.email,
                                user.nickName,
                                user.socialType,
                                user.profileImageId,
                                user.uid
                            )
                        )
                    }
                }
                result.postValue(Resource.success(null))
            }
        }
    }

}
