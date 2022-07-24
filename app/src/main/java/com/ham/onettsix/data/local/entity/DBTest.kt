package com.ham.onettsix.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class DBTest(
    @ColumnInfo(name = "name") val name: String?
) {
    @PrimaryKey
    var id: Int = 1
}

