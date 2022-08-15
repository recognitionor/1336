package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.SignIn
import com.ham.onettsix.data.model.Test
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/test/get")
    suspend fun getTest(@Body params: HashMap<String, Any?>): Test

    @POST("test/set")
    suspend fun setTest(@Body params: HashMap<String, Any?>)

    @POST("signin")
    suspend fun signIn(@Body params: HashMap<String, Any>): SignIn

    @POST("signup")
    suspend fun signUp(@Body params: HashMap<String, Any>): SignIn

}