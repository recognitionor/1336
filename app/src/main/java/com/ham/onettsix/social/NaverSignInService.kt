package com.ham.onettsix.social

import android.content.Context
import android.util.Log
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

class NaverSignInService(private val ctx: Context) : OAuthLoginCallback {

    companion object {
        const val TAG: String = "NaverSignInService"
        const val OAUTH_CLIENT_ID = "vkrfB1sRhfja9_foALjy"
        private const val OAUTH_CLIENT_SECRET = "94pfH6GiOZ"
        private const val OAUTH_CLIENT_NAME = "onettsix"
    }

    private var listener: ISocialLoginListener? = null

    fun signIn(listener: ISocialLoginListener) {
        this.listener = listener
        try {
            NaverIdLoginSDK.initialize(
                ctx,
                OAUTH_CLIENT_ID,
                OAUTH_CLIENT_SECRET,
                OAUTH_CLIENT_NAME
            )
            NaverIdLoginSDK.authenticate(ctx, this)
        } catch (e: Exception) {
            listener.onError(ISocialLoginListener.SOCIAL_TYPE_NAVER)
        }
    }

    override fun onError(errorCode: Int, message: String) {
        Log.d("jhlee", "errorCode: $errorCode - message : $message")
        listener?.onError(ISocialLoginListener.SOCIAL_TYPE_NAVER)
    }

    override fun onFailure(httpStatus: Int, message: String) {
        Log.d("jhlee", "httpStatus: $httpStatus - message : $message")
        listener?.onError(ISocialLoginListener.SOCIAL_TYPE_NAVER)
    }

    override fun onSuccess() {
        Log.d("jhlee", "onSuccess: ")
        NaverIdLoginSDK.getAccessToken()?.let {
            listener?.getToken(
                ISocialLoginListener.SOCIAL_TYPE_NAVER, it
            )
        }
    }
}