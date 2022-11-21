package com.ham.onettsix.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.StringUtil
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.api.ParamsKeys.KEY_TOKEN
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

    val refreshResult = MutableLiveData<Resource<Result>>()

    private val firebaseTokenResult = MutableLiveData<Resource<Result>>()

    private fun serFirebaseToken() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            firebaseTokenResult.postValue(Resource.error("", Result("", Pagination(), -1, "", "")))
        }
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val map = HashMap<String, Any?>()
                map[KEY_TOKEN] = preferenceHelper?.getFireBaseToken()
                apiHelper.setFirebaseToken(map)
            }
        }
    }

    fun refreshLogin() {
        refreshResult.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            refreshResult.postValue(Resource.error("", Result("", Pagination(), -1, "", "")))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val user = dbHelper.getUser()
                val accessToken = user?.accessToken ?: ""
                val refreshToken = user?.refreshToken ?: ""
                if (user != null &&
                    !TextUtils.isEmpty(accessToken) &&
                    !TextUtils.isEmpty(refreshToken)
                ) {
                    val params = HashMap<String, Any>().apply {
                        this[ParamsKeys.KEY_REFRESH_TOKEN] = refreshToken
                    }
                    val result = apiHelper.refreshAccessToken(params)

                    if (result.data != null) {
                        // refresh 를 했는데 데이터가 널이 아닌 경우 정상이므로 토큰값 업데이트 해준다.
                        RetrofitBuilder.accessToken = result.data.accessToken
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
                    } else {
                        // 만료되었다고 판단되면 유저 정보는 삭제 해준다.
                        dbHelper.deleteUser()
                    }
                    if (preferenceHelper?.getFireBaseToken()?.isNotEmpty() == true) {
                        serFirebaseToken()
                    }
                }
                refreshResult.postValue(Resource.success(null))
            }
        }
    }

}
