package com.ham.onettsix.data.api

import com.ham.onettsix.data.model.GameResult
import com.ham.onettsix.data.model.GameTypeInfo
import com.ham.onettsix.data.model.SignIn
import com.ham.onettsix.data.model.Test

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getTest(params: HashMap<String, Any?>): Test = apiService.getTest(params)

    override suspend fun setTest(params: HashMap<String, Any?>) = apiService.setTest(params)

    override suspend fun signIn(params: HashMap<String, Any>): SignIn = apiService.signIn(params)

    override suspend fun signUp(params: HashMap<String, Any>): SignIn = apiService.signUp(params)

    override suspend fun getRockPaperScissors(header: HashMap<String, Any>): GameResult =
        apiService.getRockPaperScissors(header)

    override suspend fun getGameCount(
        header: HashMap<String, Any>,
        params: HashMap<String, Any?>
    ): GameTypeInfo = apiService.getGameCount(header, params)
}