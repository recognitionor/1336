package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ham.onettsix.R
import com.ham.onettsix.adapter.TypingHistoryAdapter
import com.ham.onettsix.data.api.UrlInfo
import com.ham.onettsix.databinding.FragmentEulaBinding
import com.ham.onettsix.databinding.FragmentTypingRankBinding
import kotlin.math.abs


class TypingRankFragment() : Fragment() {

    lateinit var typingHistoryAdapter: TypingHistoryAdapter

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
        typingHistoryAdapter = TypingHistoryAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typingHistoryRv.layoutManager = LinearLayoutManager(context)
        binding.typingHistoryRv.adapter = typingHistoryAdapter
    }
}