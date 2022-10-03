package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notice(
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
) : BasicData<ArrayList<Notice.Data>>() {
    @Parcelize
    data class Data(
        @SerializedName("title")
        val title: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("iconUrl")
        val iconUrl: String,
        @SerializedName("noticedTime")
        val noticedTime: String,
        ) : Parcelable
}