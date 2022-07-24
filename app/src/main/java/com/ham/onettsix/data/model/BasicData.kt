package com.ham.onettsix.data.model

import android.os.Parcelable

abstract class BasicData<T> : Parcelable {
    abstract val description: String
    abstract val pagination: Pagination
    abstract val resultCode: String
    abstract val transactionTime: String
    abstract val data: T
}