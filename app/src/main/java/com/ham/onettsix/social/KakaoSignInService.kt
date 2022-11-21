package com.ham.onettsix.social

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class KakaoSignInService(private val ctx: Context) {
    companion object {
        const val TAG: String = "KakaoSignInService"
        const val SOCIAL_TYPE_KAKAO = "kakao"
        const val KAKAO_NATIVE_APP_KEY = "d508b76ef17b730d5e4c99875217d6f7"
    }

    private fun loginCallBack(token: OAuthToken?, error: Throwable?, listener: ISocialLoginListener) {
        if (token?.accessToken != null && token.accessToken.isNotEmpty()) {
            listener.getToken(SOCIAL_TYPE_KAKAO, token.accessToken)
        } else {
            listener.onError(SOCIAL_TYPE_KAKAO)
        }
    }

    fun signIn(listener: ISocialLoginListener) {
        Log.d("jhlee", Utility.getKeyHash((ctx)))


        if (UserApiClient.instance.isKakaoTalkLoginAvailable(ctx)) {
            UserApiClient.instance.loginWithKakaoTalk(ctx) { token, error ->
                loginCallBack(token, error, listener)
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(ctx) { token, error ->
                Log.d("jhlee", "kakao token : $token, ${error?.message}")
                loginCallBack(token, error, listener)
            }
        }
    }

    fun signOut() {
        UserApiClient.instance.logout {}
    }
}