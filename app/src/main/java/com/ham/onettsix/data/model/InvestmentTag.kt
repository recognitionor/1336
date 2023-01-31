package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InvestmentTag(
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
) : BasicData<ArrayList<InvestmentTag.Data>>() {

    @Parcelize
    data class Data(

        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String

    ) : Parcelable
}