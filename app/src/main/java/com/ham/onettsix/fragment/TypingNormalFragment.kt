package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.adapter.OnItemClickListener
import com.ham.onettsix.adapter.TypingGameNormalAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.TypingGameItem
import com.ham.onettsix.data.model.TypingGameList
import com.ham.onettsix.databinding.FragmentTypingNormalBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingNormalViewModel


class TypingNormalFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): TypingNormalFragment {
            return TypingNormalFragment()
        }
    }

    private val typingNormalViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext())),
                PreferencesHelper.getInstance(requireContext())
            )
        )[TypingNormalViewModel::class.java]
    }

    private lateinit var binding: FragmentTypingNormalBinding

    private lateinit var typeTypingGameNormalAdapter: TypingGameNormalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypingNormalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.typeTypingGameNormalAdapter =
            TypingGameNormalAdapter(object : OnItemClickListener<TypingGameItem.Data> {

                override fun onItemClick(item: TypingGameItem.Data, index: Int, view: View?) {
                    typingNormalViewModel.getTypingGame(item.questionId)
                }
            })
        setupObserver()
    }

    private fun setupObserver() {
        typingNormalViewModel.typingGameList.observe(this) {
            when (it.status) {
                Status.ERROR -> {

                }

                Status.SUCCESS -> {
                    it.data?.data?.let { list ->
                        typeTypingGameNormalAdapter.setList(list)
                        typeTypingGameNormalAdapter.notifyDataSetChanged()
                    }
                }

                Status.LOADING -> {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        binding.typingGameNormalRv.layoutManager = layoutManager
        binding.typingGameNormalRv.addItemDecoration(
            DividerItemDecoration(
                binding.typingGameNormalRv.context, layoutManager.orientation
            )
        )
        binding.typingGameNormalRv.adapter = this.typeTypingGameNormalAdapter

    }
}