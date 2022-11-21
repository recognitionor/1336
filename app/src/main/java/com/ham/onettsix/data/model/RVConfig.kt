package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class RVConfig(

    @SerializedName("network") val network: String,

    @SerializedName("appId") val appId: String,

    @SerializedName("placementId") val placementId: String,

    @SerializedName("etcConfig1") val etcConfig1: String,

    @SerializedName("etcConfig2") val etcConfig2: String
) : Parcelable
