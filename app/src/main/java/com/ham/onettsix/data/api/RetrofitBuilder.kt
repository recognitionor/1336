package com.ham.onettsix.data.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {

    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor {
                            Log.d("HttpLoggingInterceptor", "interceptor : $it")
        }

        val headerInterceptor = Interceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader(ParamsKeys.KEY_AUTH_TOKEN, accessToken).build()
            chain.proceed(request)
        }

        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(interceptor).build()

        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(UrlInfo.getBaseURL())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

    var accessToken = ""
}