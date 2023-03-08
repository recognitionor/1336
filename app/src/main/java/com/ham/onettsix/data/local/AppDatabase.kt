package com.ham.onettsix.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ham.onettsix.data.local.dao.DBNoticeDao
import com.ham.onettsix.data.local.dao.DBSearchDao
import com.ham.onettsix.data.local.dao.DBTestDao
import com.ham.onettsix.data.local.dao.DBUserDao
import com.ham.onettsix.data.local.entity.DBNotice
import com.ham.onettsix.data.local.entity.DBSearch
import com.ham.onettsix.data.local.entity.DBTest
import com.ham.onettsix.data.local.entity.DBUser

@Database(entities = [DBTest::class, DBSearch::class, DBUser::class, DBNotice::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun testDao(): DBTestDao

    abstract fun searchDao(): DBSearchDao

    abstract fun userDao(): DBUserDao

    abstract fun noticeDao(): DBNoticeDao


}