package com.ham.onettsix.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypingGameTag(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: List<Data>
) : BasicData<List<TypingGameTag.Data>>() {
    @Parcelize
    data class Data(
        @SerializedName("tagId") val tagId: Long, @SerializedName("tag") val tag: String
    ) : Parcelable
}