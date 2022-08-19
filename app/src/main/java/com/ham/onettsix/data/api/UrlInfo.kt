package com.ham.onettsix.data.api

/**
 * Created by no.1 on 2017-08-25.
 */

object UrlInfo {

    var LIVE_URL = ""
    var DEV_URL = "https://0639-175-223-26-75.jp.ngrok.io"

    @JvmStatic
    fun getBaseURL(): String {
        return DEV_URL
    }
}






