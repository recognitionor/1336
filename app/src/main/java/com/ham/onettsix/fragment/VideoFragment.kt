package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.ham.onettsix.R
import com.ham.onettsix.ad.AdmobAdapter
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Resource
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.VideoViewModel
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : Fragment(R.layout.fragment_video), View.OnClickListener {

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
        video_valid_check_btn.setOnClickListener(this)
        video_layout_img.setOnClickListener(this)

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
                    progressDialog.dismiss()
                    if (it.data?.resultCode == ResultCode.EXCEED_VIDEO_FREQUENCY) {
                        exceed_video_layout.visibility = View.VISIBLE
                        video_fragment_layout.visibility = View.GONE
                        exceed_video_tv.text = it.data.description
                    } else {
                        exceed_video_layout.visibility = View.GONE
                        video_fragment_layout.visibility = View.VISIBLE
                        it.data?.data?.let { data ->
                            video_valid_check_btn.isEnabled = true
                            video_count_info_tv.text =
                                "${data.currentRvCount}/${data.limitedRvCount}"
                        }
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
                    it.data?.data?.let { signature ->
                        Log.d("jhlee", "signature : $signature")
                        AdmobAdapter.load(requireActivity(),
                            "ca-app-pub-5672204188980144/9812109243",
                            object : RewardedAdLoadCallback() {
                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    super.onAdFailedToLoad(error)
                                    Log.d("jhlee", "error : ${error.code} -  ${error.message}")
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        R.string.video_load_fail,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onAdLoaded(rewardAd: RewardedAd) {
                                    super.onAdLoaded(rewardAd)
                                    progressDialog.dismiss()
                                    val serverSideVerificationOptions =
                                        ServerSideVerificationOptions.Builder()
                                    serverSideVerificationOptions.setUserId(signature.rvId)
                                    serverSideVerificationOptions.setCustomData(signature.signature)

                                    rewardAd.setServerSideVerificationOptions(
                                        serverSideVerificationOptions.build()
                                    )
                                    rewardAd.show(requireActivity()) { rewardItem ->
                                        if (rewardItem.amount > 0) {
                                            videoViewModel.validateLimitedRvStatus.value?.data?.data?.let { data ->
                                                video_count_info_tv.text =
                                                    "${data.currentRvCount++}/${data.limitedRvCount}"
                                            }
                                        }
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

    override fun onClick(v: View?) {
        when (v) {
            video_layout_img, video_valid_check_btn -> {
                videoViewModel.getVideoSignature()
            }
        }
    }
}
