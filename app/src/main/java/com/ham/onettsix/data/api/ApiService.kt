package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.*
import retrofit2.http.*

interface ApiService {

    @POST("/test/get")
    suspend fun getTest(@Body params: HashMap<String, Any?>): Test

    @POST("test/set")
    suspend fun setTest(@Body params: HashMap<String, Any?>)

    @POST("signin")
    suspend fun signIn(@Body params: HashMap<String, Any>): SignIn

    @POST("signup")
    suspend fun signUp(@Body params: HashMap<String, Any>): SignIn

    @POST("refreshAccessToken")
    suspend fun refreshAccessToken(@QueryMap params: HashMap<String, Any>): RefreshToken

    @GET("getRockPaperScissors")
    suspend fun getRockPaperScissors(@HeaderMap params: HashMap<String, Any>): GameResult

    @GET("getGameCount")
    suspend fun getGameCount(
        @HeaderMap header: HashMap<String, Any>,
        @QueryMap params: HashMap<String, Any?>
    ): GameTypeInfo

    @POST("attendCheck")
    suspend fun attendCheck(): Result

    @GET("validate/attendCheck")
    suspend fun validateAttendCheck(): Result

    @GET("getSignature")
    suspend fun getSignature(): Result

    @GET("/validate/limitedRv")
    suspend fun validateLimitedRv(): Result

}