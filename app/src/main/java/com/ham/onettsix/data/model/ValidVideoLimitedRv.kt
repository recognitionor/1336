package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ValidVideoLimitedRv(
    @SerializedName("description")
    override val description: String,

    @SerializedName("pagination")
    override val pagination: Pagination,

    @SerializedName("resultCode")
    override val resultCode: Int,

    @SerializedName("transactionTime")
    override val transactionTime: String,

    @SerializedName("data")
    override val data: @RawValue Data
) : BasicData<ValidVideoLimitedRv.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("limitedRvCount")
        val limitedRvCount: Int,

        @SerializedName("currentRvCount")
        var currentRvCount: Int,

    ) : Parcelable
}