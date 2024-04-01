package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.ham.onettsix.R
import com.ham.onettsix.ad.AdManager
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.FragmentVideoBinding
import com.ham.onettsix.dialog.OneButtonDialog
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.VideoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoFragment : Fragment(), View.OnClickListener, AdManager.AdManagerListener {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(layoutInflater)
        return binding.root
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
                    if (it.data?.data == null) {
                        progressDialog.dismiss()
                        OneButtonDialog(
                            getString(R.string.common_error_short), it.data?.description ?: ""
                        ) { dialog ->
                            dialog.dismiss()
                            binding.videoValidCheckBtn.text = getString(R.string.game_expire)
                            binding.videoValidCheckBtn.isEnabled = false
                        }.show(parentFragmentManager, OneButtonDialog.TAG)
                    } else {
                        binding.videoValidCheckBtn.text = getString(R.string.video_btn)
                        binding.videoValidCheckBtn.isEnabled = true
                        AdManager.getInstance().isLoaded = false
                        AdManager.getInstance().isTimeout.set(false)
                        it.data.data.let { signature ->
                            if (signature.rvConfig.isNotEmpty()) {
                                val placementId = signature.rvConfig[0].placementId
                                val timeout = signature.rvConfig[0].timeout
                                val offset = System.currentTimeMillis()
                                if (placementId.isNotEmpty()) {
                                    lifecycleScope.launch {
                                        delay(timeout.toLong())
                                        progressDialog.dismiss()
                                        AdManager.getInstance().isTimeout.set(true)
                                        if (!AdManager.getInstance().isLoaded) {
                                            onFailLoaded("Load TimeOut")
                                        }
                                    }.start()
                                    AdManager.getInstance()
                                        .load(signature, requireActivity(), this@VideoFragment)
                                    return@observe
                                }
                            }
                            progressDialog.dismiss()
                            Toast.makeText(
                                requireContext(), R.string.common_error, Toast.LENGTH_SHORT
                            ).show()
                            return@observe
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

    override fun onSuccessLoaded() {
        AdManager.getInstance().isLoaded = true
        progressDialog.dismiss()
    }

    override fun onFailLoaded(error: String) {
        AdManager.getInstance().isLoaded = false
        progressDialog.dismiss()
        Toast.makeText(
            requireContext(),
            R.string.video_load_fail,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onClosed() {
        (parentFragment as GameFragment).updateMyTicket()
        videoViewModel.validateLimitedRvStatus.value?.data?.data?.let { data ->
            binding.videoCountInfoTv.text =
                "${++data.currentRvCount}/${data.limitedRvCount}"
        }
    }
}
