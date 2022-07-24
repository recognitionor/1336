package com.ham.onettsix.data.api

object ResponseInfo {

    fun validResultCode(resultCode: String?): Boolean {
        return resultCode == RESULT_OK || resultCode == RESULT_DATE_OK
    }

    const val RESULT_DATE_OK = "resultDataOK"

    const val RESULT_OK = "resultOK"

    const val RESULT_ERROR = "resultError"

}