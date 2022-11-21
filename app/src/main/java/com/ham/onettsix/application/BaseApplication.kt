package com.ham.onettsix.application

import androidx.multidex.MultiDexApplication
import com.ham.onettsix.social.KakaoSignInService.Companion.KAKAO_NATIVE_APP_KEY
import com.kakao.sdk.common.KakaoSdk

class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
    }
}