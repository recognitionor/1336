package com.ham.onettsix.data.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class RvConfigList(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: Int,
    override val transactionTime: String,
    override val data: VideoSignature.Data
) : BasicData<VideoSignature.Data>()