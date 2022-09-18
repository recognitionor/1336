package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.R
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.dialog.WinningDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.HomeViewModel
import com.ham.onettsix.viewmodel.RPSGameViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), View.OnClickListener {

    private val homeViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        homeViewModel.lotteryInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { lotteryInfo ->
                        home_remain_time_view.setStartTime(lotteryInfo.data.limitedDate)
                        home_game_info.text =
                            getString(R.string.home_game_info, lotteryInfo.data.episode)
                        home_game_price.text =
                            getString(R.string.home_game_price, lotteryInfo.data.winningAmount)

                        home_game_ticket_info.text = getString(
                            R.string.home_game_ticket_info,
                            lotteryInfo.data.joinUserCount,
                            lotteryInfo.data.totalJoinCount
                        )

                        home_game_now_left_chance.text = getString(
                            R.string.home_game_now_left_chance,
                            lotteryInfo.data.remainLotteryCount
                        )
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getLotteryInfo()
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