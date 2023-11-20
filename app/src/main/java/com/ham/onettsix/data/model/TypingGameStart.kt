package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGameStart(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data
) : BasicData<TypingGameStart.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("questionId") var questionId: Long,
        @SerializedName("historyId") var historyId: Long
    ) : Parcelable
}