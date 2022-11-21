package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameTypeInfo(
    @SerializedName("description") override val description: String,

    @SerializedName("pagination") override val pagination: Pagination,

    @SerializedName("resultCode") override val resultCode: Int,

    @SerializedName("transactionTime") override val transactionTime: String,

    @SerializedName("data") override val data: Data
) : BasicData<GameTypeInfo.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("gameCount") val gameCount: Int,
        @SerializedName("maxCount") val maxCount: Int,
        @SerializedName("type") val type: String,
        @SerializedName("usedTicket") val usedTicket: Long,
        @SerializedName("allTicket") val allTicket: Long,
    ) : Parcelable
}