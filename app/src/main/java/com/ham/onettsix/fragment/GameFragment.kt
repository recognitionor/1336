package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.ham.onettsix.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_rps_game.*
import kotlinx.android.synthetic.main.view_attendance_layout.*
import kotlinx.android.synthetic.main.view_attendance_layout.view.*

class GameFragment : Fragment() {

    var slotTv1: Int = 0;
    var slotTv2: Int = 0;
    var slotTv3: Int = 0;

    private val gameViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[GameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("jhlee", "onCreate")

        gameViewModel.validateLimitedRv()
//        gameViewModel.getVideoSignature()
        gameViewModel.attendLoad()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
    }

    private fun setupObserve() {
        Log.d("jhlee", "setupObserve")
        gameViewModel.validateLimitedRvStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {}
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }

        gameViewModel.videoSignature.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {}
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }

        gameViewModel.attendStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.resultCode == ResultCode.DUPLICATED_ATTEND) {
                        attendance_btn.isEnabled = false
                        attendance_btn.text = it.data.description
                    } else {
                        attendance_btn.isEnabled = true
                        attendance_btn.setText(R.string.attend_btn)
                    }
                    attendance_progress.visibility = View.GONE
                    attendance_btn.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    attendance_progress.visibility = View.VISIBLE
                    attendance_btn.visibility = View.GONE
                }
                Status.ERROR -> {
                    attendance_progress.visibility = View.GONE
                    attendance_btn.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_game, null).apply {
            attendance_btn.setOnClickListener {
                gameViewModel.attendCheck()
            }
        }
    }
}
