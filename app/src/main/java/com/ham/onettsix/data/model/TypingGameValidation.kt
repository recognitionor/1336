package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGameValidation(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data,
) : BasicData<TypingGameValidation.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("maxCount") val maxCount: Int,
        @SerializedName("joinCnt") val joinCnt: Int,
        @SerializedName("remainedTicket") val remainedTicket: Int,
    ) : Parcelable
}