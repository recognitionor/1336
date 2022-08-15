package com.ham.onettsix.social

interface ISocialLoginListener {

    companion object {
        const val SOCIAL_TYPE_GOOGLE = "google"
        const val SOCIAL_TYPE_KAKAO = "kakao"
        const val SOCIAL_TYPE_NAVER = "naver"
    }

    fun getToken(socialType: String, token: String)

    fun onError(socialType: String)
}