package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RankTypingGame(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data
) : BasicData<RankTypingGame.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("episode") val episode: Int,
        @SerializedName("totalJoinUserCount") val totalJoinUserCount: Int,
        @SerializedName("content") val content: String,
        @SerializedName("endDate") val endDate: Long,
        @SerializedName("typingGameHistoryResList") val typingGameHistoryResList: List<RankGameTypingGameHistoryResList>
    ) : Parcelable {
        @Parcelize
        data class RankGameTypingGameHistoryResList(@SerializedName("episode") val episode: Int) :
            Parcelable
    }
}