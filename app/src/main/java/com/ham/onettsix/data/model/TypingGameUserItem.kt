package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGameUserItem(
    @SerializedName("userId") var userId: Long,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("duration") var duration: Long,
    @SerializedName("gameOrder") var gameOrder: Long
) : Parcelable {

}