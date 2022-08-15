package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.SignIn
import com.ham.onettsix.data.model.Test

interface ApiHelper {

    suspend fun getTest(params: HashMap<String, Any?>): Test?

    suspend fun setTest(params: HashMap<String, Any?>)

    suspend fun signIn(params: HashMap<String, Any>): SignIn?

    suspend fun signUp(params: HashMap<String, Any>): SignIn?

}