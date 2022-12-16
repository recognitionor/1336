package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.LoadAdError
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
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.FragmentAttendanceBinding
import com.ham.onettsix.databinding.FragmentVideoBinding
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.VideoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoFragment : Fragment(R.layout.fragment_video), View.OnClickListener {

    private lateinit var binding: FragmentVideoBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentVideoBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()

        if (PreferencesHelper.getInstance(requireContext()).isLogin()) {
            binding.videoFragmentLayout.visibility = View.VISIBLE
            binding.layoutVideoNeededLogin.layoutGameNeededLogin.visibility = View.GONE
        } else {
            binding.videoFragmentLayout.visibility = View.GONE
            binding.layoutVideoNeededLogin.layoutGameNeededLogin.visibility = View.VISIBLE
        }
        videoViewModel.validateLimitedRv()
        binding.layoutVideoNeededLogin.layoutGameNeededLogin.setOnClickListener(this)
        binding.videoValidCheckBtn.setOnClickListener(this)
        binding.videoLayoutImg.setOnClickListener(this)

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
                        binding.exceedVideoTv.text = it.data.description
                    } else {
                        it.data?.data?.let { data ->
                            binding.videoValidCheckBtn.isEnabled = true
                            binding.videoCountInfoTv.text =
                                "${data.currentRvCount}/${data.limitedRvCount}"
                        }
                    }
                }
                Status.ERROR -> {
                    binding.videoValidCheckBtn.isEnabled = false
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
                        if (signature.rvConfig.isNotEmpty()) {
                            var isTimeout = false
                            val placementId = signature.rvConfig[0].placementId
                            if (placementId.isNotEmpty()) {
                                lifecycleScope.launch {
                                    delay(30000L)
                                    isTimeout = true
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        R.string.video_load_fail,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                AdmobAdapter.load(requireActivity(),
                                    signature.rvConfig[0].placementId,
                                    object : RewardedAdLoadCallback() {
                                        override fun onAdFailedToLoad(error: LoadAdError) {
                                            super.onAdFailedToLoad(error)
                                            if (!isTimeout) {
                                                progressDialog.dismiss()
                                                Toast.makeText(
                                                    requireContext(),
                                                    R.string.video_load_fail,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                        override fun onAdLoaded(rewardAd: RewardedAd) {
                                            super.onAdLoaded(rewardAd)
                                            if (!isTimeout) {
                                                progressDialog.dismiss()
                                                val serverSideVerificationOptions =
                                                    ServerSideVerificationOptions.Builder()
                                                serverSideVerificationOptions.setUserId(signature.rvId)
                                                serverSideVerificationOptions.setCustomData(
                                                    signature.signature
                                                )

                                                rewardAd.setServerSideVerificationOptions(
                                                    serverSideVerificationOptions.build()
                                                )
                                                rewardAd.show(requireActivity()) { _ ->
                                                    (parentFragment as GameFragment).updateMyTicket()
                                                    videoViewModel.validateLimitedRvStatus.value?.data?.data?.let { data ->
                                                        binding.videoCountInfoTv.text =
                                                            "${++data.currentRvCount}/${data.limitedRvCount}"
                                                    }
                                                }
                                            }
                                        }
                                    })
                            }
                        }
                    }
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

            binding.videoLayoutImg, binding.videoValidCheckBtn -> {
                videoViewModel.getVideoSignature()
            }
            binding.layoutVideoNeededLogin.layoutGameNeededLogin -> {
                (parentFragment as GameFragment).login()
            }

        }
    }

    fun loginUpdate() {
        if (PreferencesHelper.getInstance(requireContext()).isLogin()) {
            binding.videoFragmentLayout.visibility = View.VISIBLE
            binding.layoutVideoNeededLogin.layoutGameNeededLogin.visibility = View.GONE
        } else {
            binding.videoFragmentLayout.visibility = View.GONE
            binding.layoutVideoNeededLogin.layoutGameNeededLogin.visibility = View.VISIBLE
        }
    }
}
