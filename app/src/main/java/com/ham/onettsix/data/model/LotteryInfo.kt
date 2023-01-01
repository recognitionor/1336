package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LotteryInfo(
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
) : BasicData<LotteryInfo.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("lotteryType")
        val lotteryType: String,

        @SerializedName("episode")
        val episode: Int,

        @SerializedName("limitedDate")
        val limitedDate: Long,

        @SerializedName("nextEpisodeStartDate")
        val nextEpisodeStartDate: Long,

        @SerializedName("winningAmount")
        val winningAmount: Int,

        @SerializedName("joinUserCount")
        val joinUserCount: Long,

        @SerializedName("totalJoinCount")
        val totalJoinCount: Long,

        @SerializedName("remainLotteryCount")
        val remainLotteryCount: Long,

        @SerializedName("nickName")
        val nickName: String,

        @SerializedName("userId")
        val userId: String,

        @SerializedName("profileImageId")
        val profileImageId: Int,

    ) : Parcelable
}