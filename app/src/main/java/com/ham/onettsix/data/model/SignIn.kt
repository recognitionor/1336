package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignIn(
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
) : BasicData<SignIn.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("tokenSet")
        val tokenSet: TokenSet,
        @SerializedName("email")
        val email: String,
        @SerializedName("profileImageId")
        val profileImageId: Int,
        @SerializedName("nickName")
        val nickName: String,
        @SerializedName("socialType")
        var socialType: String,
        @SerializedName("socialAccessToken")
        var socialAccessToken: String,
        @SerializedName("uid")
        val uid: Int,
        @SerializedName("needSignUp")
        val needSignUp: Boolean
    ) : Parcelable
}