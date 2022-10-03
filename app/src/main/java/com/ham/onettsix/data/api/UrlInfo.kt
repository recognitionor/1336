package com.ham.onettsix.data.api

/**
 * Created by no.1 on 2017-08-25.
 */

object UrlInfo {

    var LIVE_URL = "https://api.onettsix.com"
    var DEV_URL = "https://7bad-106-245-0-178.jp.ngrok.io"


    @JvmStatic
    fun getBaseURL(): String {
        return DEV_URL
    }
}






