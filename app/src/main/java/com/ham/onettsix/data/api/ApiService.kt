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

    @POST("signout")
    suspend fun signOut(@QueryMap params: HashMap<String, Any>): Result


    @POST("refreshAccessToken")
    suspend fun refreshAccessToken(@QueryMap params: HashMap<String, Any>): RefreshToken

    @GET("getRockPaperScissors")
    suspend fun getRockPaperScissors(): GameResult

    @GET("getGameCount")
    suspend fun getGameCount(
        @QueryMap params: HashMap<String, Any?>
    ): GameTypeInfo


    @GET("getSlot")
    suspend fun getSlot(): Result

    @POST("attendCheck")
    suspend fun attendCheck(): Result

    @GET("validate/attendCheck")
    suspend fun validateAttendCheck(): Result

    @GET("getSignature")
    suspend fun getSignature(): VideoSignature

    @GET("/validate/limitedRv")
    suspend fun validateLimitedRv(): ValidVideoLimitedRv

    @GET("getLotteryInfo/{type}")
    suspend fun getLotteryInfo(@Path(value = "type") type: String): LotteryInfo

    @POST("getTicket")
    suspend fun getTicket(@Body params: HashMap<String, Any?>): Result

    @GET("createLottery")
    suspend fun createLottery(): Result

    @GET("getInstantLottery")
    suspend fun getInstantLottery(): Result

    @POST("getLottery")
    suspend fun getLottery(@Body params: HashMap<String, Any?>): Result

    @POST("requestTicket")
    suspend fun requestTicket(@Body params: HashMap<String, Any?>): Result

    @GET("me/history")
    suspend fun getHistoryInfo(): HistoryInfo

    @GET("lotteryHistory")
    suspend fun getLotteryHistory(@QueryMap params: HashMap<String, Any?>): LotteryHistory

    @GET("/get/notices")
    suspend fun getNoticeList(): Notice

    @POST("firebasetoken")
    suspend fun setFirebaseToken(@QueryMap params: HashMap<String, Any?>): Result


}