package com.ham.onettsix.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RefreshToken(
    @SerializedName("description")
    override val description: String,

    @SerializedName("pagination")
    override val pagination: Pagination,

    @SerializedName("resultCode")
    override val resultCode: Int,

    @SerializedName("transactionTime")
    override val transactionTime: String,

    @SerializedName("data")
    override val data: TokenSet? = null
) : BasicData<TokenSet?>() {
}