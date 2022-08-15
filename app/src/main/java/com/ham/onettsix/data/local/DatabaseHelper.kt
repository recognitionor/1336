package com.ham.onettsix.data.local

import com.ham.onettsix.data.local.entity.DBSearch
import com.ham.onettsix.data.local.entity.DBTest
import com.ham.onettsix.data.local.entity.DBUser

interface DatabaseHelper {


    suspend fun deleteTest(test: DBTest)

    suspend fun insertSearchKeyword(searchKeyword: DBSearch)

    suspend fun deleteAllSearchKeyword()

    suspend fun getSearchKeyword(searchKeyword: String): List<DBSearch>

    suspend fun getUser(): DBUser

    suspend fun insertUser(user: DBUser)

    suspend fun deleteUser()
}