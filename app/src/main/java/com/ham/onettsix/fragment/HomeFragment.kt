package com.ham.onettsix.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.LoginActivity
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.adapter.HomeGameProgressAdapter
import com.ham.onettsix.adapter.RecyclerDecorationWidth
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.constant.ResultCode.LOTTERY_FINISHED_LOSE
import com.ham.onettsix.constant.ResultCode.LOTTERY_FINISHED_WIN
import com.ham.onettsix.constant.ResultCode.LOTTERY_INFO_PROCEEDING
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.FragmentHomeBinding
import com.ham.onettsix.dialog.*
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.HomeViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapter: HomeGameProgressAdapter

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
                homeViewModel.getLotteryInfo()
                homeViewModel.getGameTypeInfo()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.homeGameProgressRv.adapter = adapter
        binding.homeGameProgressRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.homeGameProgressRv.addItemDecoration(
            RecyclerDecorationWidth(
                resources.getDimension(
                    R.dimen.rv_divider_width
                )
            )
        )
        binding.topMenu.setOnClickListener {
            (activity as MainActivity).run {
                this.selectedItem(2)
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        adapter = HomeGameProgressAdapter()
    }

    @SuppressLint("ResourceAsColor")
    private fun setupObserver() {
        homeViewModel.episodeList.observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let { list ->
                        adapter.setItemList(list)
                    }
                    adapter.notifyDataSetChanged()
                    binding.homeGameProgressRv.scrollToPosition(adapter.getCurrentGamePosition())
                }

                Status.ERROR -> {
                }

                Status.LOADING -> {
                }
            }
        }
        homeViewModel.gameTypeInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                }
            }
        }
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
                        if (lotteryInfo.resultCode == ResultCode.LOTTERY_INFO_PROCEEDING) {
                            binding.homeGameTicketOpenPercent.visibility = View.VISIBLE
                            binding.homeGameRemainTicketInfoLayout.visibility = View.VISIBLE
                            val ratePercent: Float =
                                ((lotteryInfo.data.totalJoinCount.toFloat() / (lotteryInfo.data.remainLotteryCount + lotteryInfo.data.totalJoinCount)) * 100).toFloat()
                            binding.homeGameTicketParticipationRate.text = "$ratePercent%"
                            binding.homeRemainTimeView.setStartTime(lotteryInfo.data.limitedDate)
                            binding.homeGameCurrentTicketInfo.text = getString(
                                R.string.home_game_current_ticket_info,
                                lotteryInfo.data.remainLotteryCount + lotteryInfo.data.totalJoinCount,
                                lotteryInfo.data.totalJoinCount
                            )
                        } else {
                            binding.homeGameTicketOpenPercent.visibility = View.GONE
                            binding.homeGameRemainTicketInfoLayout.visibility = View.GONE
                            binding.homeRemainTimeView.setStartTime(lotteryInfo.data.nextEpisodeStartDate)
                        }
                        binding.homeGameStatusLayout.homeGameStatusWonPrice.text = getString(
                            R.string.home_game_status_won_price, "${lotteryInfo.data.winningAmount}"
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
        homeViewModel.getNewNotice()
        homeViewModel.getGameTypeInfo()
        homeViewModel.getEpisodeList()

        binding.homeGameGetTicketBtn.setOnClickListener(this)
        binding.homeGameHelp1.setOnClickListener(this)
        binding.homeGameHelp2.setOnClickListener(this)
        binding.homeGameHelp3.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.homeGameHelp1 -> {
                (activity as MainActivity).run {
                    this.selectedItem(1)
                }
            }

            binding.homeGameHelp2 -> {
                binding.homeGameGetTicketBtn.performClick()
            }

            binding.homeGameHelp3 -> {
                (activity as MainActivity).run {
                    this.selectedItem(3)
                }
            }

            binding.homeGameGetTicketBtn -> {
                if (PreferencesHelper.getInstance(requireActivity()).isLogin()) {
                    homeViewModel.gameTypeInfo.value?.data?.data?.let { data ->
                        val remainTicket = data.allTicket - data.usedTicket
                        val remainChance =
                            homeViewModel.lotteryInfo.value?.data?.data?.remainLotteryCount ?: 0
                        if (homeViewModel.lotteryInfo.value?.data?.resultCode != LOTTERY_INFO_PROCEEDING) {
                            Toast.makeText(
                                requireContext(),
                                requireContext().getString(R.string.try_next_time),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        if (remainTicket > 0) {
                            homeViewModel.lotteryInfo.value?.data?.data?.remainLotteryCount
                            ChallengeGameDialog(
                                remainTicket.toInt(), remainChance
                            ) { isPositive: Boolean, remainTicket: Int, dialog: DialogFragment ->
                                if (isPositive) {
                                    homeViewModel.getInstanceLottery(remainTicket)
                                }
                                dialog.dismiss()
                            }.show(parentFragmentManager, ChallengeGameDialog.TAG)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                requireContext().getString(R.string.no_ticket),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    OneButtonDialog(content = getString(R.string.login_is_required)) { dialog ->
                        dialog.dismiss()
                        activityResult.launch(Intent(requireActivity(), LoginActivity::class.java))
                    }.show(parentFragmentManager, OneButtonDialog.TAG)
                }
            }
        }
    }

    fun refresh() {
        homeViewModel.getGameTypeInfo()
        homeViewModel.getLotteryInfo()
    }
}