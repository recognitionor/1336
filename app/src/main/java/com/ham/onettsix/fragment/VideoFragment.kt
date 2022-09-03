package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.ham.onettsix.R
import com.ham.onettsix.ad.AdmobAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.VideoViewModel
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : Fragment(R.layout.fragment_video) {

    private val videoViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[VideoViewModel::class.java]
    }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.getInstance(parentFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
        videoViewModel.validateLimitedRv()
        video_layout_img.setOnClickListener {
            Log.d("jhlee", "videoViewModel.getVideoSignature()")
            progressDialog.show()
            AdmobAdapter.load(requireActivity(), "ca-app-pub-5672204188980144/9812109243",
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                    }

                    override fun onAdLoaded(p0: RewardedAd) {
                        super.onAdLoaded(p0)
                        progressDialog.dismiss()
                        p0.show(requireActivity()) {

                        }
                    }
                })

//            videoViewModel.getVideoSignature()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
        videoViewModel.videoSignature.removeObservers(viewLifecycleOwner)
        videoViewModel.validateLimitedRvStatus.removeObservers(viewLifecycleOwner)
    }

    private fun setupObserve() {
        videoViewModel.validateLimitedRvStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let { data ->
                        progressDialog.dismiss()
                        video_valid_check_btn.isEnabled = true
                        video_count_info_tv.text = "${data.currentRvCount}/${data.limitedRvCount}"
                    }
                }
                Status.ERROR -> {
                    video_valid_check_btn.isEnabled = false
                    progressDialog.dismiss()
                }
                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }

        videoViewModel.videoSignature.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("jhlee", "success")
                    it.data?.data?.let {
                        AdmobAdapter.load(
                            requireActivity(),
                            "ca-app-pub-5672204188980144/9812109243",
                            object : RewardedAdLoadCallback() {
                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    super.onAdFailedToLoad(error)
                                    progressDialog.dismiss()
                                }

                                override fun onAdLoaded(rewardAd: RewardedAd) {
                                    super.onAdLoaded(rewardAd)
                                    progressDialog.dismiss()
                                    rewardAd.show(requireActivity()) { rewardAd ->
                                        Log.d(
                                            "jhlee",
                                            "reward : ${rewardAd.type}, ${rewardAd.amount}"
                                        )
                                    }
                                }
                            })
                    }
                    Log.d("jhlee", "success : ${it.data?.data}")
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                }
                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }
}
