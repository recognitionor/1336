package com.ham.onettsix.data.api

/**
 * Created by no.1 on 2017-08-25.
 */

object UrlInfo {

    var LIVE_URL = "https://api.onettsix.com"
    //https://api.winning-lotto.com

            private var DEV_URL = "https://api.onettsix.com"
//    private var DEV_URL = "https://cfa4-2001-2d8-2021-a271-5448-1ce0-3144-931b.ngrok-free.app"

    @JvmStatic
    fun getBaseURL(): String {
        return DEV_URL
    }
}




