package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ham.onettsix.R
import com.ham.onettsix.dialog.WinningDialog
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home_game_get_ticket_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            home_game_get_ticket_btn -> {
                WinningDialog().show(parentFragmentManager, WinningDialog.TAG)
            }
        }
    }
}