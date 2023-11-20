package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGameItem(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data
) : BasicData<TypingGameItem.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("questionId") var questionId: Long,
        @SerializedName("totalJoinCnt") var totalJoinCnt: Long,
        @SerializedName("userId") var userId: Long,
        @SerializedName("nickname") var nickname: String,
        @SerializedName("duration") var duration: Long,
        @SerializedName("content") var content: String,
        @SerializedName("userOrders") var userOrders: List<TypingGameUserItem>
    ) : Parcelable
}