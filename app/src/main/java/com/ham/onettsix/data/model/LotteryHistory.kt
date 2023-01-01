package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class LotteryHistory(
    @SerializedName("description")
    override val description: String,

    @SerializedName("pagination")
    override val pagination: Pagination,

    @SerializedName("resultCode")
    override val resultCode: Int,

    @SerializedName("transactionTime")
    override val transactionTime: String,

    @SerializedName("data")
    override val data: ArrayList<Data> = arrayListOf()
) : BasicData<ArrayList<LotteryHistory.Data>>() {

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

        @SerializedName("memberCount")
        val memberCount: Int,

        @SerializedName("endDate")
        val endDate: String,

        @SerializedName("nickname")
        val nickname: String,

        @SerializedName("content")
        val content: String

        ) : Parcelable
}