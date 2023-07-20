package com.ham.onettsix.data.local.dao

import androidx.room.*
import com.ham.onettsix.data.local.entity.DBUser

@Dao
interface DBUserDao {

    @Query("SELECT * FROM DBUser WHERE id = 1")
    fun getUser(): DBUser

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: DBUser)

    @Update
    fun update(user: DBUser)

    @Query("DELETE FROM DBUser")
    fun deleteUser()
}