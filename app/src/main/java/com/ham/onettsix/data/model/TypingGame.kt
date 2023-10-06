package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGame(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data
) : BasicData<TypingGame.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("questionId") var questionId: Int,
        @SerializedName("episode") var episode: Int,
        @SerializedName("content") var content: String,
        @SerializedName("useAt") var useAt: Boolean,
        @SerializedName("rankingType") var rankingType: String,
        @SerializedName("gameType") var gameType: String,
        @SerializedName("limitedTime") var limitedTime: Long,
        @SerializedName("typingHistoryList") var typingHistoryList: List<TypingHistory>
    ) : Parcelable
}