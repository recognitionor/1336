package com.ham.onettsix.fragment

import androidx.fragment.app.Fragment
import com.ham.onettsix.R
import kotlinx.android.synthetic.main.fragment_ticket_status.*

class TicketStatusFragment : Fragment(R.layout.fragment_ticket_status) {

    fun updateStatusText(statusTest: String) {
        my_ticket_status_tv.text = statusTest
    }

}