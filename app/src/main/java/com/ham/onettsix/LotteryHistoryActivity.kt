package com.ham.onettsix

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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

class LotteryHistoryActivity : AppCompatActivity(R.layout.activity_lottery_history),
    View.OnClickListener {

    private val lotteryHistoryViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
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

        setupObserver()
        lottery_history_toolbar_back.setOnClickListener {
            finish()
        }
        lottery_history_winner_rv.layoutManager = LinearLayoutManager(this)
        lottery_history_winner_rv.adapter = lotteryHistoryAdapter
        lotteryHistoryViewModel.getLotteryHistoryList("ALL", 1)
    }

    private fun setupObserver() {
        lotteryHistoryViewModel.lotteryHistoryList.observe(this) {
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

    override fun onClick(view: View) {
        when (view) {
        }
    }
}
