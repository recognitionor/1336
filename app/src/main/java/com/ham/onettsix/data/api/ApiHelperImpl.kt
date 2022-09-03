package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.*

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getTest(params: HashMap<String, Any?>): Test = apiService.getTest(params)

    override suspend fun setTest(params: HashMap<String, Any?>) = apiService.setTest(params)

    override suspend fun signIn(params: HashMap<String, Any>): SignIn = apiService.signIn(params)

    override suspend fun signUp(params: HashMap<String, Any>): SignIn = apiService.signUp(params)

    override suspend fun signOut(params: HashMap<String, Any>): Result = apiService.signOut(params)

    override suspend fun refreshAccessToken(params: HashMap<String, Any>): RefreshToken =
        apiService.refreshAccessToken(params)

    override suspend fun attendCheck(): Result = apiService.attendCheck()

    override suspend fun validateAttendCheck(): Result = apiService.validateAttendCheck()

    override suspend fun validateLimitedRv(): ValidVideoLimitedRv = apiService.validateLimitedRv()

    override suspend fun getSignature(): Result = apiService.getSignature()

    override suspend fun getRockPaperScissors(): GameResult = apiService.getRockPaperScissors()

    override suspend fun getGameCount(params: HashMap<String, Any?>): GameTypeInfo =
        apiService.getGameCount(params)

    override suspend fun getSlot(): Result = apiService.getSlot()

    override suspend fun getLotteryInfo(type: String): LotteryInfo = apiService.getLotteryInfo(type)

    override suspend fun getLottery(params: HashMap<String, Any?>): Result =
        apiService.getLottery(params)

    override suspend fun requestTicket(params: HashMap<String, Any?>): Result =
        apiService.requestTicket(params)

    override suspend fun createLottery(): Result = apiService.createLottery()

    override suspend fun getInstantLottery(): Result = apiService.getInstantLottery()

    override suspend fun getHistoryInfo(): HistoryInfo = apiService.getHistoryInfo()


}