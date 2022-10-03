package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.LotteryHistoryViewModel
import kotlinx.android.synthetic.main.activity_lottery_history.*
import kotlinx.android.synthetic.main.fragment_history.*

class LotteryHistoryFragment : Fragment(R.layout.fragment_history) {

    private val lotteryHistoryViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext())),
                PreferencesHelper.getInstance(requireContext())
            )
        )[LotteryHistoryViewModel::class.java]
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
            }
        }

    private lateinit var lotteryHistoryAdapter: LotteryHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lotteryHistoryAdapter = LotteryHistoryAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        lottery_history_winner_rv.layoutManager = LinearLayoutManager(requireContext())
        lottery_history_winner_rv.adapter = lotteryHistoryAdapter
        lotteryHistoryViewModel.getLotteryHistoryList("ALL", 1)
    }

    private fun setupObserver() {
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, null)
    }
}
