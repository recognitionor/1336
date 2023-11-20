package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingRandomGame(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data
) : BasicData<TypingRandomGame.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("questionId") val questionId: Int,
        @SerializedName("content") val content: String,
        @SerializedName("limitedTime") val limitedTime: Int,
        @SerializedName("useAt") val useAt: Boolean
    ) : Parcelable
}