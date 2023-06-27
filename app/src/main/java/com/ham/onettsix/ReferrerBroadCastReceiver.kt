package com.ham.onettsix

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.ParamsKeys
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReferrerBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        GlobalScope.launch {

            val referrerClient: InstallReferrerClient =
                InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    val exceptionHandler = CoroutineExceptionHandler { _, e ->
                    }
                    GlobalScope.launch(exceptionHandler) {
                        val params = HashMap<String, Any?>()
                        val msg = intent?.getStringExtra("referrer") ?: ""
                        params[ParamsKeys.KEY_REFERRER_CONTENT] = "$responseCode : $msg"
                        params[ParamsKeys.KEY_REFERRER_STATUS] = 1
                        ApiHelperImpl(RetrofitBuilder.apiService).setReferrer(params)
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }
    }
}