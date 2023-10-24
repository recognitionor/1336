package com.ham.onettsix.data.api

/**
 * Created by no.1 on 2017-08-25.
 */

object UrlInfo {

    var LIVE_URL = "https://api.onettsix.com"
    private var DEV_URL = "https://api.winning-lotto.com"

    @JvmStatic
    fun getBaseURL(): String {
        return DEV_URL
    }
}






