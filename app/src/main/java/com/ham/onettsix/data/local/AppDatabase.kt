package com.ham.onettsix.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ham.onettsix.data.local.dao.DBSearchDao
import com.ham.onettsix.data.local.dao.DBTestDao
import com.ham.onettsix.data.local.dao.DBUserDao
import com.ham.onettsix.data.local.entity.DBSearch
import com.ham.onettsix.data.local.entity.DBTest
import com.ham.onettsix.data.local.entity.DBUser

@Database(entities = [DBTest::class, DBSearch::class, DBUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun testDao(): DBTestDao

    abstract fun searchDao(): DBSearchDao

    abstract fun userDao(): DBUserDao


}