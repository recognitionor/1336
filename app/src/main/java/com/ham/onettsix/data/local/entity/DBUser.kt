package com.ham.onettsix.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"], unique = true)])
data class DBUser(
    @ColumnInfo(name = "accessToken") val accessToken: String?,
    @ColumnInfo(name = "refreshToken") val refreshToken: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "nickName") val nickName: String?,
    @ColumnInfo(name = "socialType") val socialType: String?,
    @ColumnInfo(name = "profileImageId") val profileImageId: Int?,
    @ColumnInfo(name = "uid") val uid: Int?
) {
    @PrimaryKey
    var id: Int = 1
}

