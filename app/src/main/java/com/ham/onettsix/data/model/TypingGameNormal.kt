package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypingGameNormal(
    @SerializedName("creator") val creator: String,

    @SerializedName("attendCount") val attendCount: Int,

    @SerializedName("title") val title: String,

    ) : Parcelable
