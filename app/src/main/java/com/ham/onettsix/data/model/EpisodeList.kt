package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodeList(
    @SerializedName("description") override val description: String,

    @SerializedName("pagination") override val pagination: Pagination,

    @SerializedName("resultCode") override val resultCode: Int,

    @SerializedName("transactionTime") override val transactionTime: String,

    @SerializedName("data") override val data: ArrayList<Data> = arrayListOf()

) : BasicData<ArrayList<EpisodeList.Data>>() {

    @Parcelize
    data class Data(

        @SerializedName("userId")
        val userId: Int,

        @SerializedName("nickName")
        val nickName: String,

        @SerializedName("episode")
        val episode: Int,

        @SerializedName("winningAmount")
        val winningAmount: Int,

        @SerializedName("isWinning")
        val isWinning: String,

        @SerializedName("isEnd")
        val isEnd: Boolean,

        @SerializedName("episodeStatus")
        val episodeStatus: Int,

        @SerializedName("status")
        val status: String,

        @SerializedName("startDate")
        val startDate: Long,

        @SerializedName("endDate")
        val endDate: Long,

        @SerializedName("cnt")
        val cnt: String,

        ) : Parcelable
}
