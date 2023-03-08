package com.ham.onettsix.data.local.dao

import androidx.room.*
import com.ham.onettsix.data.local.entity.DBNotice
import com.ham.onettsix.data.local.entity.DBSearch

@Dao
interface DBNoticeDao {

    @Query("SELECT * FROM DBSearch WHERE keyword = (:noticeId)")
    fun getNoticeId(noticeId: String): List<DBNotice>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNoticeId(noticeId: DBNotice)
}