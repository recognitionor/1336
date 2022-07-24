package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pagination(
    @SerializedName("currentElements")
    val currentElements: Int = 0,
    @SerializedName("currentPage")
    val currentPage: Int = 0,
    @SerializedName("totalElements")
    val totalElements: Int = 0,
    @SerializedName("totalPages")
    val totalPages: Int = 0
) : Parcelable