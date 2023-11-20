package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGameRankMain(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data
) : BasicData<TypingGameRankMain.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("content") var content: String,
        @SerializedName("endDate") var endDate: Long,
        @SerializedName("episode") var episode: Int,
        @SerializedName("questionId") var questionId: Long,
        @SerializedName("totalJoinUserCount") var totalJoinUserCount: Long,
        @SerializedName("typingGameHistoryResList") var typingGameHistoryResList: List<TypingGameHistoryResItem>
    ) : Parcelable {
        @Parcelize
        data class TypingGameHistoryResItem(
            @SerializedName("duration") var duration: Long,
            @SerializedName("nickname") var nickname: String,
            @SerializedName("totalJoinCnt") var totalJoinCnt: Int,
            @SerializedName("userId") var userId: Long
        ) : Parcelable
    }
}