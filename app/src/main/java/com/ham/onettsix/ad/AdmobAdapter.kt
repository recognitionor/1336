package com.ham.onettsix.ad

import android.app.Activity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.ham.onettsix.data.model.RVConfig
import com.ham.onettsix.data.model.VideoSignature


class AdmobAdapter {
    //"ca-app-pub-5672204188980144/9812109243"
    companion object {
        private var instance: AdmobAdapter? = null
        fun getInstance(): AdmobAdapter {
            if (instance == null) {
                instance = AdmobAdapter()
            }
            return instance as AdmobAdapter
        }
    }

    fun loadBanner(activity: Activity, adView: AdView) {
        MobileAds.initialize(activity) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
    }

    fun load(
        activity: Activity,
        placementId: String,
        rvId: String,
        signature: String,
        listener: AdManager.AdManagerListener
    ) {
        MobileAds.initialize(
            activity
        ) {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                activity,
                placementId,
                adRequest,
                object : RewardedAdLoadCallback() {

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        listener.onFailLoaded(error.message)
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        super.onAdLoaded(rewardedAd)
                        val serverSideVerificationOptions = ServerSideVerificationOptions.Builder()
                        serverSideVerificationOptions.setUserId(rvId)
                        serverSideVerificationOptions.setCustomData(signature)
                        listener.onSuccessLoaded()
                        rewardedAd.setServerSideVerificationOptions(serverSideVerificationOptions.build())
                        rewardedAd.show(
                            activity
                        ) {}
                    }
                })
        }
    }

    fun load(
        activity: Activity,
        videoData: VideoSignature.Data,
        rvConfig: RVConfig,
        listener: AdManager.AdManagerListener
    ) {
        MobileAds.initialize(
            activity
        ) {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                activity,
                rvConfig.placementId,
                adRequest,
                object : RewardedAdLoadCallback() {

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        listener.onFailLoaded(error.message)
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        super.onAdLoaded(rewardedAd)
                        val serverSideVerificationOptions = ServerSideVerificationOptions.Builder()
                        serverSideVerificationOptions.setUserId(videoData.rvId)
                        serverSideVerificationOptions.setCustomData(videoData.signature)
                        listener.onSuccessLoaded()
                        rewardedAd.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    listener.onClosed()
                                }
                            }
                        rewardedAd.setServerSideVerificationOptions(serverSideVerificationOptions.build())
                        rewardedAd.show(
                            activity
                        ) {

                        }
                    }
                })
        }
    }
}