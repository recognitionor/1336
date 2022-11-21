package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryInfo(
    @SerializedName("description")
    override val description: String = "",

    @SerializedName("pagination")
    override val pagination: Pagination,

    @SerializedName("resultCode")
    override val resultCode: Int = -1,

    @SerializedName("transactionTime")
    override val transactionTime: String = "",

    @SerializedName("data")
    override val data: ArrayList<Data> = arrayListOf()

) : BasicData<ArrayList<HistoryInfo.Data>>() {
    @Parcelize
    data class Data(
        @SerializedName("isWinning")
        val isWinning: Boolean,

        @SerializedName("grade")
        val grade: Int,

        @SerializedName("userId")
        val userId: Int,

        @SerializedName("episode")
        val episode: Int,

        @SerializedName("winningAmount")
        val winningAmount: Int,

        @SerializedName("isEnd")
        val isEnd: Boolean,

        @SerializedName("endDate")
        val endDate: String,

        @SerializedName("nickname")
        val nickname: String,

        @SerializedName("isPaid")
        val isPaid: Boolean,

    ) : Parcelable
}