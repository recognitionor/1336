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
import kotlinx.android.synthetic.main.fragment_attendance.*

class AttendFragment : Fragment(R.layout.fragment_attendance) {
    private val attendViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[AttendViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
        attendViewModel.attendStatusLoad()

        attendance_btn.setOnClickListener {
            attendViewModel.attendCheck()
        }
    }

    private fun setupObserve() {
        attendViewModel.attendStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.resultCode == ResultCode.DUPLICATED_ATTEND) {
                        attendance_btn.isEnabled = false
                        attendance_btn.text = getString(R.string.attend_done)
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
}
