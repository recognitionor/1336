package com.ham.onettsix.ad

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdmobAdapter {
    //"ca-app-pub-5672204188980144/9812109243"
    companion object {
        fun load(
            activity: Activity,
            adId: String,
            callback: RewardedAdLoadCallback
        ) {
            MobileAds.initialize(
                activity
            ) {
                val adRequest = AdRequest.Builder().build()
                RewardedAd.load(
                    activity,
                    adId,
                    adRequest,
                    callback
                )
            }
        }
    }
}