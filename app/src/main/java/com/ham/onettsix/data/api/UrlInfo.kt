package com.ham.onettsix.data.api

/**
 * Created by no.1 on 2017-08-25.
 */

object UrlInfo {

    var LIVE_URL = "https://api.onettsix.com"
    var DEV_URL = "https://f736-121-190-40-38.jp.ngrok.io"


    @JvmStatic
    fun getBaseURL(): String {
        return DEV_URL
    }
}






