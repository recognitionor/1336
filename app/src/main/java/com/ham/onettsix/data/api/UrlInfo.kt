package com.ham.onettsix.data.api

/**
 * Created by no.1 on 2017-08-25.
 */

object UrlInfo {

    var LIVE_URL = ""
    var DEV_URL = "https://a11c-112-168-227-219.jp.ngrok.io"

    @JvmStatic
    fun getBaseURL(): String {
        return DEV_URL
    }
}






