package com.ham.onettsix

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.adapter.TypingGameMyInfoAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityTypingInfoBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingGameMyInfoViewModel

class TypingGameInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingInfoBinding

    private lateinit var adapter: TypingGameMyInfoAdapter

    private val typingGameMyInfoViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[TypingGameMyInfoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typingGameMyInfoViewModel.getMyPage()
        adapter = TypingGameMyInfoAdapter()
        binding = ActivityTypingInfoBinding.inflate(layoutInflater)

        binding.typingGameRegisterToolbarBack.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
        binding.typingGameMyInfoRv.layoutManager = LinearLayoutManager(this)
        binding.typingGameMyInfoRv.adapter = adapter
        setupObserver()
    }

    private fun setupObserver() {
        typingGameMyInfoViewModel.myInfo.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.typingGameMyInfoEmptyView.visibility = View.GONE
                    adapter.setList(result.data)
                    adapter.notifyDataSetChanged()
                }

                Status.LOADING -> {

                }

                Status.ERROR -> {

                }
            }
        }
    }
}