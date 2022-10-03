package com.ham.onettsix

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.adapter.LotteryHistoryAdapter
import com.ham.onettsix.adapter.NoticeAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.LotteryHistoryViewModel
import com.ham.onettsix.viewmodel.NoticeViewModel
import kotlinx.android.synthetic.main.activity_lottery_history.*
import kotlinx.android.synthetic.main.activity_notice.*

class NoticeActivity : AppCompatActivity(R.layout.activity_notice),
    View.OnClickListener {

    private val noticeViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[NoticeViewModel::class.java]
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
            }
        }

    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        noticeViewModel.getNoticeList()
        notice_toolbar_back.setOnClickListener { finish() }
        noticeAdapter = NoticeAdapter()
        notice_rv.layoutManager = LinearLayoutManager(this)
        notice_rv.adapter = noticeAdapter
    }

    private fun setupObserver() {
        noticeViewModel.notice.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let {list->
                        noticeAdapter.setItemList(list)
                        noticeAdapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view) {
        }
    }
}
