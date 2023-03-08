package com.ham.onettsix.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewNotice(
    @SerializedName("description") override val description: String,

    @SerializedName("pagination") override val pagination: Pagination,

    @SerializedName("resultCode") override val resultCode: Int,

    @SerializedName("transactionTime") override val transactionTime: String,

    @SerializedName("data") override val data: HashSet<Int> = HashSet()

) : BasicData<HashSet<Int>>()