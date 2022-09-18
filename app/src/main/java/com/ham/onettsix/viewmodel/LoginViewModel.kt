package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.api.ParamsKeys.KEY_ACCESS_TOKEN
import com.ham.onettsix.data.api.ParamsKeys.KEY_ENABLE_ALARM
import com.ham.onettsix.data.api.ParamsKeys.KEY_SOCIAL_TYPE
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.SignIn
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val signIn = MutableLiveData<Resource<SignIn>>()

    fun signIn(socialType: String, token: String) {
        signIn.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.d("jhlee", "error : ${e.message}")
            signIn.postValue(Resource.error("signin error", null))
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                signIn.postValue(Resource.loading(null))
                val hashMap = HashMap<String, Any>().apply {
                    this[KEY_SOCIAL_TYPE] = socialType
                    this[KEY_ACCESS_TOKEN] = token
                    this[KEY_ENABLE_ALARM] = true
                }
                var result = apiHelper.signIn(hashMap)
                val needSigninUp = result?.data?.needSignUp == true
                if (needSigninUp) {
                    result = apiHelper.signUp(hashMap)
                    return@withContext
                }
                result?.data?.let {
                    RetrofitBuilder.accessToken = it.tokenSet.accessToken
                    dbHelper.insertUser(
                        DBUser(
                            it.tokenSet.accessToken,
                            it.tokenSet.refreshToken,
                            it.email,
                            it.nickName,
                            it.socialType,
                            it.profileImageId,
                            it.uid
                        )
                    )
                    /*로그인 성공 했으니 firebase token 갱신 필요*/
                    val map = HashMap<String, Any?>()
                    map[ParamsKeys.KEY_TOKEN] = preferenceHelper?.getFireBaseToken()
                    apiHelper.setFirebaseToken(map)

                    signIn.postValue(Resource.success(result))
                    return@withContext
                }
                throw Exception("에러~~!~!")
            }
        }
    }
}
