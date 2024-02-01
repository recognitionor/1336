package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypingHistory(
    @SerializedName("ranking") val ranking: Int,

    @SerializedName("user") val user: String,

    @SerializedName("record") val record: Float,

    @SerializedName("count") val count: Int,

    ) : Parcelable
