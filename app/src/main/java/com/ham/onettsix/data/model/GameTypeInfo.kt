package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameTypeInfo(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: Data
) : BasicData<GameTypeInfo.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("gameCount")
        val gameCount: Int,
        val maxCount: Int,
        val type: String
    ) : Parcelable
}