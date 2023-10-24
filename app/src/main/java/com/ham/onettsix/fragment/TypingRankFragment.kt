package com.ham.onettsix.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.TypingGameActivity
import com.ham.onettsix.adapter.OnItemClickListener
import com.ham.onettsix.adapter.TypingHistoryAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.TypingHistory
import com.ham.onettsix.databinding.FragmentTypingRankBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingNormalViewModel


class TypingRankFragment : Fragment() {

    private val typingReadyViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext())),
                PreferencesHelper.getInstance(requireContext())
            )
        )[TypingNormalViewModel::class.java]
    }

    private lateinit var typingHistoryAdapter: TypingHistoryAdapter

    companion object {
        @JvmStatic
        fun newInstance(): TypingRankFragment {
            return TypingRankFragment()
        }
    }

    private lateinit var binding: FragmentTypingRankBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypingRankBinding.inflate(layoutInflater)
        binding.typingHistoryRv.adapter = return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typingHistoryAdapter = TypingHistoryAdapter(object : OnItemClickListener<TypingHistory> {
            override fun onItemClick(item: TypingHistory, index: Int, view: View?) {
                binding.typingBottomAlert.show(item)
            }
        })
        setupObserver()

    }

    private fun setupObserver() {
        typingReadyViewModel.typingGameList.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    typingHistoryAdapter
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typingHistoryRv.layoutManager = LinearLayoutManager(context)
        binding.typingHistoryRv.adapter = typingHistoryAdapter
        binding.typingGameRankLayout.setOnClickListener {
            startActivity(Intent(requireContext(), TypingGameActivity::class.java))
        }
    }
}