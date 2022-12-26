package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YouTubeInfo(
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
) : BasicData<ArrayList<YouTubeInfo.Data>>() {

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
        @SerializedName("youtubeLink")
        val youtubeLink: String,
        ) : Parcelable
}