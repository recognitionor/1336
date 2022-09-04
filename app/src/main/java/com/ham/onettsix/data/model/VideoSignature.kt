package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class VideoSignature(
    @SerializedName("description")
    override val description: String,

    @SerializedName("pagination")
    override val pagination: Pagination,

    @SerializedName("resultCode")
    override val resultCode: Int,

    @SerializedName("transactionTime")
    override val transactionTime: String,

    @SerializedName("data")
    override val data: Data
) : BasicData<VideoSignature.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("signature")
        val signature: String,

        @SerializedName("rvId")
        val rvId: String

    ) : Parcelable
}