package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InvestmentInfo(
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
) : BasicData<ArrayList<InvestmentInfo.Data>>() {

    @Parcelize
    data class Data(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("youtubeId")
        val youtubeId: String,
        @SerializedName("thumbnailLink")
        val thumbnailLink: String,
        @SerializedName("link")
        val youtubeLink: String,
        @SerializedName("type")
        val type: Int,
        ) : Parcelable
}