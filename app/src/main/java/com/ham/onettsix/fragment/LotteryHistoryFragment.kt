package com.ham.onettsix.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.R
import com.ham.onettsix.adapter.LotteryHistoryAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.FragmentHistoryBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.LotteryHistoryViewModel

class LotteryHistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private val lotteryHistoryViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext())),
                PreferencesHelper.getInstance(requireContext())
            )
        )[LotteryHistoryViewModel::class.java]
    }

    private lateinit var lotteryHistoryAdapter: LotteryHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lotteryHistoryAdapter = LotteryHistoryAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()

        binding.lotteryHistoryWinnerRv.layoutManager = LinearLayoutManager(requireContext())
        binding.lotteryHistoryWinnerRv.adapter = lotteryHistoryAdapter
        lotteryHistoryViewModel.getLotteryHistoryList("ALL", 1)
        lotteryHistoryViewModel.getLotteryInfo()
    }

    private fun setupObserver() {
        lotteryHistoryViewModel.lotteryInfo.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let { data ->
                        binding.lotteryHistoryCount.post {
                            binding.lotteryHistoryCount.text = requireActivity().getString(R.string.lottery_history_count, data.episode)
                        }
                        binding.lotteryHistoryWinningPrice.post{
                            binding.lotteryHistoryWinningPrice.text = "${requireActivity().getString(R.string.lottery_history_total_price)} ${data.winningAmount}"
                        }

                        if (TextUtils.isEmpty(data.userId)) {
                            binding.lotteryHistoryTitle.visibility = View.GONE
                            binding.lotteryHistoryProfileImg.visibility = View.GONE
                            binding.lotteryHistoryWinningBtn.visibility = View.GONE
                            binding.lotteryHistoryWinnerId.post {
                                binding.lotteryHistoryWinnerId.text = requireActivity().getString(R.string.lottery_history_next_time)
                            }
                        } else {
                            binding.lotteryHistoryTitle.visibility = View.VISIBLE
                            binding.lotteryHistoryProfileImg.visibility = View.VISIBLE
                            binding.lotteryHistoryWinningBtn.visibility = View.VISIBLE
                            binding.lotteryHistoryWinnerId.post {
                                binding.lotteryHistoryWinnerId.text = "${data.nickName}#${data.userId}"
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
        lotteryHistoryViewModel.lotteryHistoryList.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("jhlee", "Status.SUCCESS")
                    it?.data?.let { list ->
                        lotteryHistoryAdapter.setItemList(list)
                    }
                    lotteryHistoryAdapter.notifyDataSetChanged()
                }

                Status.ERROR -> {
                    Log.d("jhlee", "Status.ERROR")
                }

                Status.LOADING -> {
                    Log.d("jhlee", "Status.LOADING")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }
}
