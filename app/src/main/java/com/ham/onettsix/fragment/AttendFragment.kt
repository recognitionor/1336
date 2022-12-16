package com.ham.onettsix.fragment

import android.os.Bundle
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
import com.ham.onettsix.databinding.FragmentAttendanceBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.AttendViewModel

class AttendFragment : Fragment() {

    private lateinit var binding: FragmentAttendanceBinding

    private val attendViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[AttendViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
        attendViewModel.attendStatusLoad()

        binding.attendanceBtn.setOnClickListener {
            attendViewModel.attendCheck()
        }
    }

    private fun setupObserve() {
        attendViewModel.attendStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.resultCode == ResultCode.SUCCESS_ATTEND || it.data?.resultCode == ResultCode.DUPLICATED_ATTEND) {
                        binding.attendanceBtn.isEnabled = false
                        binding.attendanceBtn.text = getString(R.string.attend_done)
                    } else {
                        binding.attendanceBtn.isEnabled = true
                        binding.attendanceBtn.setText(R.string.attend_btn)
                    }
                    binding.attendanceProgress.visibility = View.GONE
                    binding.attendanceBtn.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    binding.attendanceProgress.visibility = View.VISIBLE
                    binding.attendanceBtn.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.attendanceProgress.visibility = View.GONE
                    binding.attendanceBtn.visibility = View.GONE
                }
            }
        }
    }
}
