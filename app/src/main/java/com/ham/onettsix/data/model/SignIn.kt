package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignIn(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: String,
    override val transactionTime: String,
    override val data: Data
) : BasicData<SignIn.Data>() {
    @Parcelize
    data class Data(
        @SerializedName("tokenSet")
        val tokenSet: TokenSet,
        @SerializedName("email")
        val email: String,
        @SerializedName("nickName")
        val nickName: String,
        @SerializedName("socialType")
        val socialType: String,
        @SerializedName("uid")
        val uid: Int,
        @SerializedName("profileImageId")
        val profileImageId: Int,
        @SerializedName("needSignUp")
        val needSignUp: Boolean
    ) : Parcelable
}