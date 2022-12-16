package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ham.onettsix.R
import com.ham.onettsix.databinding.FragmentAttendanceBinding
import com.ham.onettsix.databinding.FragmentTicketStatusBinding

class TicketStatusFragment : Fragment() {

    private lateinit var binding: FragmentTicketStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicketStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    fun updateStatusText(statusTest: String) {
        binding.myTicketStatusTv.text = statusTest
    }
}