package com.ham.onettsix.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ham.onettsix.R
import com.ham.onettsix.databinding.FragmentAttendanceBinding
import com.ham.onettsix.databinding.FragmentTicketStatusBinding

class TicketStatusFragment : Fragment(R.layout.fragment_ticket_status) {

    private lateinit var binding: FragmentTicketStatusBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTicketStatusBinding.inflate(layoutInflater)
    }

    fun updateStatusText(statusTest: String) {
        binding.myTicketStatusTv.text = statusTest
    }

}