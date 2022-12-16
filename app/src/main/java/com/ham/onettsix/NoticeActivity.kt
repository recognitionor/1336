package com.ham.onettsix

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.adapter.NoticeAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityNoticeBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.NoticeViewModel

class NoticeActivity : AppCompatActivity(R.layout.activity_notice),
    View.OnClickListener {

    private lateinit var binding: ActivityNoticeBinding

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
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        noticeViewModel.getNoticeList()

        binding.noticeToolbarBack.setOnClickListener { finish() }
        noticeAdapter = NoticeAdapter()
        binding.noticeRv.layoutManager = LinearLayoutManager(this)
        binding.noticeRv.adapter = noticeAdapter
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
