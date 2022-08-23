package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.*

interface ApiHelper {

    suspend fun getTest(params: HashMap<String, Any?>): Test?

    suspend fun setTest(params: HashMap<String, Any?>)

    suspend fun signIn(params: HashMap<String, Any>): SignIn?

    suspend fun signUp(params: HashMap<String, Any>): SignIn?

    suspend fun refreshAccessToken(params: HashMap<String, Any>): RefreshToken

    suspend fun attendCheck(): Result

    suspend fun validateAttendCheck(): Result

    suspend fun validateLimitedRv(): Result

    suspend fun getSignature(): Result

    suspend fun getRockPaperScissors(): GameResult

    suspend fun getGameCount(params: HashMap<String, Any?>): GameTypeInfo

    suspend fun getSlot(): Result

    suspend fun getLotteryInfo(type: String): Result


}