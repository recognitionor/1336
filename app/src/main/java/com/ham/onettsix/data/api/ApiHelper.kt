package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.*

interface ApiHelper {

    suspend fun getTest(params: HashMap<String, Any?>): Test?

    suspend fun setReferrer(params: HashMap<String, Any?>)

    suspend fun setTest(params: HashMap<String, Any?>)

    suspend fun signIn(params: HashMap<String, Any>): SignIn?

    suspend fun signUp(params: HashMap<String, Any>): SignIn?

    suspend fun signOut(params: HashMap<String, Any>): Result

    suspend fun refreshAccessToken(params: HashMap<String, Any>): RefreshToken

    suspend fun attendCheck(): Result

    suspend fun validateAttendCheck(): Result

    suspend fun validateLimitedRv(): ValidVideoLimitedRv

    suspend fun getSignature(): VideoSignature

    suspend fun getRockPaperScissors(): GameResult

    suspend fun getGameCount(params: HashMap<String, Any?>): GameTypeInfo

    suspend fun getSlot(): Result

    suspend fun getLotteryInfo(type: String): LotteryInfo

    suspend fun getLotteryHistory(params: HashMap<String, Any?>): LotteryHistory

    suspend fun getLottery(params: HashMap<String, Any?>): Result

    suspend fun requestTicket(params: HashMap<String, Any?>): Result

    suspend fun createLottery(): Result

    suspend fun getInstantLottery(params: HashMap<String, Any?>): Result

    suspend fun getHistoryInfo(): HistoryInfo

    suspend fun getNoticeList(): Notice

    suspend fun getNewNotice(): NewNotice

    suspend fun setFirebaseToken(params: HashMap<String, Any?>): Result

    suspend fun getInvestmentList(params: HashMap<String, Any?>): InvestmentInfo

    suspend fun getInvestmentTagList(): InvestmentTag

    suspend fun withDraw(): Result

    suspend fun changeAlarm(params: HashMap<String, Any?>): Result

    suspend fun getSecretCode(params: HashMap<String, Any?>): WinnerSecretCode

    suspend fun getRewardUnit(): RewardUnit

    suspend fun getEpisodeList(): EpisodeList

    suspend fun getTypingGameList(params: HashMap<String, Any?>): TypingGameList

    suspend fun getTypingGame(params: HashMap<String, Any?>): TypingGameItem.Data

    suspend fun getTypingGameByRanking(params: HashMap<String, Any?>): TypingGameList

    suspend fun endTypingGame(
        gameType: String, historyId: Long, duration: Long, questionId: Long
    ): TypingGameList

    suspend fun startTypingGame(gameType: String, questionId: String): TypingGameList

}