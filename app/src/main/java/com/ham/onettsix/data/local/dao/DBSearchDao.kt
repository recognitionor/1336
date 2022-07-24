package com.ham.onettsix.data.local.dao

import androidx.room.*
import com.ham.onettsix.data.local.entity.DBSearch

@Dao
interface DBSearchDao {

    @Query("SELECT * FROM DBSearch WHERE keyword = (:searchKeyword)")
    fun getSearchKeyword(searchKeyword: String): List<DBSearch>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSearchKeyword(searchKeyword: DBSearch)

    @Query("DELETE FROM DBSearch")
    fun deleteAllSearchKeyword()
}