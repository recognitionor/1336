package com.ham.onettsix.ad

import android.app.Activity
import android.util.Log
import com.ham.onettsix.data.model.VideoSignature
import java.util.concurrent.atomic.AtomicBoolean

class AdManager private constructor() {

    var isTimeout: AtomicBoolean = AtomicBoolean(false)

    var isLoaded = false

    var isShowing = false

    interface AdManagerListener {
        fun onSuccessLoaded()
        fun onFailLoaded(error: String)
        fun onClosed()
    }

    companion object {
        const val ADMOB = "admob"
        const val IRONSOURCE = "ironsource"

        private var instance: AdManager? = null

        fun getInstance(): AdManager {
            if (instance == null) {
                instance = AdManager()
            }
            return instance as AdManager
        }
    }

    fun load(videoData: VideoSignature.Data, activity: Activity, listener: AdManagerListener) {
        videoData.rvConfig[0].let {
            when (it.network) {
                ADMOB -> {
                    AdmobAdapter.getInstance()
                        .load(activity, videoData, it, listener)
                }

                IRONSOURCE -> {
                    IronSourceAdapter.getInstance()
                        .load(activity, videoData, it, listener)
                }
            }
        }
    }
}