package com.ham.onettsix.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseBuilder {

    private var INSTANCE: AppDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE IF EXISTS DBNotice");
//            database.execSQL("CREATE TABLE `DBNotice` (`id` INTEGER, `noticeId` TEXT, " +
//                    "PRIMARY KEY(`id`))")
        }
    }

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private val migration1to2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `DBNotice` " +
                        "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`noticeId` TEXT NOT NULL, " +
                        "UNIQUE(`noticeId`))"
            )
        }
    }


    private fun buildRoomDB(context: Context) = Room.databaseBuilder(
        context.applicationContext, AppDatabase::class.java, context.packageName
    ).addMigrations(migration1to2).build()

}