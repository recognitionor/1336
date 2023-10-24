package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.*

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getTest(params: HashMap<String, Any?>): Test = apiService.getTest(params)
    override suspend fun setReferrer(params: HashMap<String, Any?>) = apiService.setReferrer(params)
    override suspend fun setTest(params: HashMap<String, Any?>) = apiService.setTest(params)

    override suspend fun signIn(params: HashMap<String, Any>): SignIn = apiService.signIn(params)

    override suspend fun signUp(params: HashMap<String, Any>): SignIn = apiService.signUp(params)

    override suspend fun signOut(params: HashMap<String, Any>): Result = apiService.signOut(params)

    override suspend fun refreshAccessToken(params: HashMap<String, Any>): RefreshToken =
        apiService.refreshAccessToken(params)

    override suspend fun attendCheck(): Result = apiService.attendCheck()

    override suspend fun validateAttendCheck(): Result = apiService.validateAttendCheck()

    override suspend fun validateLimitedRv(): ValidVideoLimitedRv = apiService.validateLimitedRv()

    override suspend fun getSignature(): VideoSignature = apiService.getSignature()

    override suspend fun getRockPaperScissors(): GameResult = apiService.getRockPaperScissors()

    override suspend fun getGameCount(params: HashMap<String, Any?>): GameTypeInfo =
        apiService.getGameCount(params)

    override suspend fun getSlot(): Result = apiService.getSlot()

    override suspend fun getLotteryInfo(type: String): LotteryInfo = apiService.getLotteryInfo(type)

    override suspend fun getLotteryHistory(params: HashMap<String, Any?>): LotteryHistory =
        apiService.getLotteryHistory(params)

    override suspend fun getLottery(params: HashMap<String, Any?>): Result =
        apiService.getLottery(params)

    override suspend fun requestTicket(params: HashMap<String, Any?>): Result =
        apiService.requestTicket(params)

    override suspend fun createLottery(): Result = apiService.createLottery()

    override suspend fun getInstantLottery(params: HashMap<String, Any?>): Result =
        apiService.getInstantLottery(params)

    override suspend fun getHistoryInfo(): HistoryInfo = apiService.getHistoryInfo()

    override suspend fun getNoticeList(): Notice = apiService.getNoticeList()

    override suspend fun getNewNotice(): NewNotice = apiService.getNewNotice()
    override suspend fun setFirebaseToken(params: HashMap<String, Any?>): Result =
        apiService.setFirebaseToken(params)

    override suspend fun getInvestmentList(params: HashMap<String, Any?>): InvestmentInfo =
        apiService.getInvestmentList(params)

    override suspend fun getInvestmentTagList(): InvestmentTag = apiService.getInvestmentTagList()

    override suspend fun withDraw(): Result = apiService.withDraw()

    override suspend fun changeAlarm(params: HashMap<String, Any?>): Result =
        apiService.changeAlarm(params)

    override suspend fun getSecretCode(params: HashMap<String, Any?>): WinnerSecretCode =
        apiService.getSecretCode(params)

    override suspend fun getRewardUnit(): RewardUnit = apiService.getRewardUnit()
    override suspend fun getEpisodeList(): EpisodeList = apiService.getEpisodeList()
    override suspend fun getTypingGameList(params: HashMap<String, Any?>): TypingGameList =
        apiService.getTypingGameList(params)

    override suspend fun getTypingGame(params: HashMap<String, Any?>): TypingGameItem.Data =
        apiService.getTypingGame(params)

    override suspend fun getTypingGameByRanking(params: HashMap<String, Any?>): TypingGameList =
        apiService.getTypingGameByRanking(params)

    override suspend fun endTypingGame(
        gameType: String, historyId: Long, duration: Long, questionId: Long
    ): TypingGameList = apiService.endTypingGame(gameType, historyId, duration, questionId)

    override suspend fun startTypingGame(gameType: String, questionId: String): TypingGameList =
        apiService.startTypingGame(gameType, questionId)

}