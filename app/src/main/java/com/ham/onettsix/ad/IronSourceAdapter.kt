package com.ham.onettsix.ad

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.ham.onettsix.data.model.RVConfig
import com.ham.onettsix.data.model.VideoSignature
import com.ironsource.adapters.supersonicads.SupersonicConfig
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.RewardedVideoManualListener

class IronSourceAdapter private constructor() {

    companion object {

        private const val APP_KEY = "191981e7d"

        private var instance: IronSourceAdapter? = null

        fun getInstance(): IronSourceAdapter {
            if (instance == null) {
                instance = IronSourceAdapter()
            }
            return instance as IronSourceAdapter
        }
    }

    lateinit var callback: RewardedAdLoadCallback

    fun load(
        activity: Activity,
        videoData: VideoSignature.Data,
        rvConfig: RVConfig,
        listener: AdManager.AdManagerListener
    ) {
        IronSource.init(
            activity, APP_KEY, {
                IronSource.setManualLoadRewardedVideo(object : RewardedVideoManualListener {

                    override fun onRewardedVideoAdOpened() {
                    }

                    override fun onRewardedVideoAdClosed() {
                        listener.onClosed()
                    }

                    override fun onRewardedVideoAvailabilityChanged(isReady: Boolean) {
                        if (isReady) {
                            listener.onSuccessLoaded()
                            IronSource.setDynamicUserId(videoData.rvId)
                            if (!AdManager.getInstance().isTimeout.get()) {
                                IronSource.showRewardedVideo(rvConfig.placementId)
                            }
                        }
                    }

                    override fun onRewardedVideoAdStarted() {
                    }

                    override fun onRewardedVideoAdEnded() {
                    }

                    override fun onRewardedVideoAdRewarded(placement: Placement?) {
                    }

                    override fun onRewardedVideoAdShowFailed(error: IronSourceError?) {
                    }

                    override fun onRewardedVideoAdClicked(placement: Placement?) {
                    }

                    override fun onRewardedVideoAdReady() {

                    }

                    override fun onRewardedVideoAdLoadFailed(error: IronSourceError?) {
                        listener.onFailLoaded(error?.errorMessage ?: "")
                    }
                })
                IronSource.loadRewardedVideo()
            }, IronSource.AD_UNIT.REWARDED_VIDEO
        )
    }
}