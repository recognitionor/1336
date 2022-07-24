package com.ham.onettsix.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["keyword"], unique = true)])
data class DBSearch(
    @ColumnInfo(name = "keyword") val keyword: String?,
    @ColumnInfo(name = "type") val type: Int?
) {
    @PrimaryKey
    var id: Int = 1
}

