package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGameMyInfo(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: List<Data>
) : BasicData<List<TypingGameMyInfo.Data>>() {
    @Parcelize
    data class Data(
        @SerializedName("questionId") val questionId: Long,
        @SerializedName("userId") val userId: Long,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("duration") val duration: Long,
        @SerializedName("content") val content: String,
        @SerializedName("gameType") val gameType: String,
        @SerializedName("gameOrder") val gameOrder: Int
    ) : Parcelable
}