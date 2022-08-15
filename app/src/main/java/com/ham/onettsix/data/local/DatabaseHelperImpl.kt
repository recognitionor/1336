package com.ham.onettsix.data.local

import com.ham.onettsix.data.local.entity.DBSearch
import com.ham.onettsix.data.local.entity.DBTest
import com.ham.onettsix.data.local.entity.DBUser

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun deleteTest(test: DBTest) {
    }

    override suspend fun insertSearchKeyword(searchKeyword: DBSearch) =
        appDatabase.searchDao().insertSearchKeyword(searchKeyword)

    override suspend fun deleteAllSearchKeyword() =
        appDatabase.searchDao().deleteAllSearchKeyword()

    override suspend fun getSearchKeyword(searchKeyword: String) =
        appDatabase.searchDao().getSearchKeyword(searchKeyword)

    override suspend fun getUser(): DBUser = appDatabase.userDao().getUser()

    override suspend fun insertUser(user: DBUser) = appDatabase.userDao().insertUser(user)

    override suspend fun deleteUser() = appDatabase.userDao().deleteUser()

}