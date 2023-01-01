package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WinnerSecretCode(
    @SerializedName("description") override val description: String,

    @SerializedName("pagination") override val pagination: Pagination,

    @SerializedName("resultCode") override val resultCode: Int,

    @SerializedName("transactionTime") override val transactionTime: String,

    @SerializedName("data") override val data: Data
) : BasicData<WinnerSecretCode.Data>() {

    @Parcelize
    data class Data(
        @SerializedName("grade") val grade: Int,
        @SerializedName("winningAmount") val winningAmount: Int,
        @SerializedName("isPaid") val isPaid: Boolean,
        @SerializedName("secretCode") val secretCode: String,
        @SerializedName("userId") val userId: Int,
        @SerializedName("episode") val episode: Long,
    ) : Parcelable
}