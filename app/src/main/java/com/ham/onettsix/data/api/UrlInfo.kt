package com.ham.onettsix.data.api

/**
 * Created by no.1 on 2017-08-25.
 */

object UrlInfo {

    var LIVE_URL = "https://api.onettsix.com"
    var DEV_URL = "https://b264-221-141-225-219.jp.ngrok.io"

    @JvmStatic
    fun getBaseURL(): String {
        return DEV_URL
    }
}






