package com.ham.onettsix.data.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
    override val description: String,
    override val pagination: Pagination,
    override val resultCode: String,
    override val transactionTime: String,
    override val data: String
    ) : BasicData<String>()