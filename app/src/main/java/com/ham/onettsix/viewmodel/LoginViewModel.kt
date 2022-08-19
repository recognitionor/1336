package com.ham.onettsix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.api.ParamsKeys.KEY_ACCESS_TOKEN
import com.ham.onettsix.data.api.ParamsKeys.KEY_AUTH_TOKEN
import com.ham.onettsix.data.api.ParamsKeys.KEY_ENABLE_ALARM
import com.ham.onettsix.data.api.ParamsKeys.KEY_ENABLE_ALERT
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
import java.lang.Exception

class LoginViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    val signIn = MutableLiveData<Resource<SignIn>>()

    fun signIn(socialType: String, token: String) {
        Log.d("jhlee", "signIn")
        signIn.postValue(Resource.loading(null))
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            signIn.postValue(Resource.error("signin error", null))
            Log.d("jhlee", "error : ${e.message}")
        }

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                signIn.postValue(Resource.loading(null))
                val hashMap = HashMap<String, Any>().apply {
                    this[KEY_SOCIAL_TYPE] = socialType
                    this[KEY_ACCESS_TOKEN] = token
                    this[KEY_ENABLE_ALARM] = true
                }
                Log.d("jhlee", "signIn :")
                var result = apiHelper.signIn(hashMap)
                Log.d("jhlee", "signInResult : $result")
                val needSigninUp = result?.data?.needSignUp == true
                if (needSigninUp) {
                    result = apiHelper.signUp(hashMap)
                    return@withContext
                }
                result?.data?.let {
                    RetrofitBuilder.accessToken = it.tokenSet.accessToken
                    Log.d("jhlee", "it.tokenSet.accessToken : ${it.tokenSet.accessToken}")
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
                    signIn.postValue(Resource.success(result))
                    return@withContext
                }
                throw Exception("에러~~!~!")
            }
        }
    }
}
