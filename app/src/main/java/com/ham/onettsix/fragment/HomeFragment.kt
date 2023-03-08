package com.ham.onettsix.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.ham.onettsix.LoginActivity
import com.ham.onettsix.LotteryHistoryActivity
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.constant.ResultCode.LOTTERY_FINISHED_LOSE
import com.ham.onettsix.constant.ResultCode.LOTTERY_FINISHED_WIN
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.FragmentHomeBinding
import com.ham.onettsix.dialog.DialogDismissListener
import com.ham.onettsix.dialog.OneButtonDialog
import com.ham.onettsix.dialog.TwoButtonDialog
import com.ham.onettsix.dialog.WinningDialog
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.HomeViewModel


class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    val homeViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[HomeViewModel::class.java]
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ActivityResultKey.LOGIN_RESULT_OK) {
                (this@HomeFragment.activity as MainActivity).mainViewModel.updateUserInfo()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        homeViewModel.winningViewModel.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    when (it.data?.resultCode) {
                        LOTTERY_FINISHED_WIN, LOTTERY_FINISHED_LOSE -> {
                            WinningDialog(it.data, object : DialogDismissListener {
                                override fun onDismissListener() {
                                    homeViewModel.getLotteryInfo()
                                }
                            }).show(parentFragmentManager, WinningDialog.TAG)
                        }
                        else -> {
                            OneButtonDialog("", it.data?.description ?: "") { dialog ->
                                dialog.dismiss()
                            }.show(parentFragmentManager, OneButtonDialog.TAG)
                        }
                    }
                }
                else -> {}
            }
        }
        homeViewModel.lotteryInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { lotteryInfo ->
                        binding.homeGameInfo.text =
                            getString(R.string.home_game_info, lotteryInfo.data.episode)
                        binding.homeGamePrice.text =
                            getString(R.string.home_game_price, lotteryInfo.data.winningAmount)
                        if (lotteryInfo.resultCode == ResultCode.LOTTERY_INFO_PROCEEDING) {
                            binding.homeGameSixSixManQuestionImg.visibility = View.VISIBLE
                            binding.homeRemainDrawingTimeTitle.text =
                                getString(R.string.home_remain_drawing_time_title)
                            binding.homeRemainTimeView.setStartTime(lotteryInfo.data.limitedDate)
                            binding.homeGameTicketInfo.text = getString(
                                R.string.home_game_ticket_info,
                                lotteryInfo.data.joinUserCount,
                                lotteryInfo.data.totalJoinCount
                            )
                            binding.homeGameNowLeftChance.text = getString(
                                R.string.home_game_now_left_chance,
                                lotteryInfo.data.remainLotteryCount
                            )
                            binding.homeGameNowLeftChance.visibility = View.VISIBLE
                            binding.homeGameTicketInfo.visibility = View.VISIBLE
                            binding.homeGameWhoIsLucky.text =
                                getString(R.string.home_game_who_is_lucky_who)
                        } else {
                            binding.homeGameSixSixManQuestionImg.visibility = View.GONE
                            binding.homeRemainDrawingTimeTitle.text =
                                getString(R.string.home_remain_drawing_next_time_title)
                            binding.homeRemainTimeView.setStartTime(lotteryInfo.data.nextEpisodeStartDate)
                            binding.homeRemainTimeView.stopTime()
                            binding.homeGameNowLeftChance.visibility = View.GONE
                            binding.homeGameTicketInfo.visibility = View.GONE
                            if (TextUtils.isEmpty(lotteryInfo.data.userId)) {
                                binding.homeGameWhoIsLucky.text =
                                    getString(R.string.home_game_who_is_lucky_next)
                            } else {
                                Glide.with(this)
                                    .load(ProfileImageUtil.getImageId(lotteryInfo.data.profileImageId))
                                    .into(binding.homeGameSixSixManImg)
                                val preString = getString(R.string.home_game_who_is_lucky)
                                val ssb =
                                    SpannableStringBuilder("$preString ${lotteryInfo.data.nickName}#${lotteryInfo.data.userId}")
                                ssb.setSpan(
                                    ForegroundColorSpan(R.color.main_color),
                                    preString.length,
                                    ssb.length,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                binding.homeGameWhoIsLucky.text = ssb
                            }
                        }
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
        homeViewModel.getNewNotice()

        binding.homeGameGetTicketBtn.setOnClickListener(this)
        binding.homeGameGoHistoryTitle.setOnClickListener(this)
        binding.homeGameGoHistoryForwardImg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.homeGameGetTicketBtn -> {
                if (PreferencesHelper.getInstance(requireActivity()).isLogin()) {
                    TwoButtonDialog(
                        getString(R.string.game_try_dialog_title),
                        getString(R.string.game_try_dialog_content)
                    ) { isPositive: Boolean, dialog: DialogFragment ->
                        if (isPositive) {
                            homeViewModel.getInstanceLottery()
                        }
                        dialog.dismiss()
                    }.show(parentFragmentManager, TwoButtonDialog.TAG)
                } else {
                    OneButtonDialog(content = getString(R.string.login_is_required)) { dialog ->
                        dialog.dismiss()
                        activityResult.launch(Intent(requireActivity(), LoginActivity::class.java))
                    }.show(parentFragmentManager, OneButtonDialog.TAG)
                }
            }

            binding.homeGameGoHistoryTitle, binding.homeGameGoHistoryForwardImg -> {
                activityResult.launch(
                    Intent(
                        this.requireActivity(), LotteryHistoryActivity::class.java
                    )
                )
            }
        }
    }
}