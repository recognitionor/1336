package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.R
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.AttendViewModel
import com.ham.onettsix.viewmodel.VideoViewModel
import kotlinx.android.synthetic.main.fragment_attendance.*

class VideoFragment : Fragment(R.layout.fragment_video) {

    private val videoViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(
            ApiHelperImpl(RetrofitBuilder.apiService),
            DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
        )
        )[VideoViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
        videoViewModel.validateLimitedRv()
    }

    private fun setupObserve() {
        videoViewModel.validateLimitedRvStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {}
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }

        videoViewModel.videoSignature.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {}
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }
}
