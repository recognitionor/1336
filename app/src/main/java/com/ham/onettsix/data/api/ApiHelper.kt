package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.Test

interface ApiHelper {

    suspend fun getTest(params: HashMap<String, Any?>): Test?

    suspend fun setTest(params: HashMap<String, Any?>)

}