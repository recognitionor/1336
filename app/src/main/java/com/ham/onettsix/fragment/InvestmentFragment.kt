package com.ham.onettsix.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.adapter.InvestmentAdapter
import com.ham.onettsix.adapter.InvestmentTagAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.model.InvestmentInfo
import com.ham.onettsix.data.model.InvestmentTag
import com.ham.onettsix.databinding.FragmentInvestmentBinding
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.InvestmentViewModel

class InvestmentFragment : Fragment() {

    private lateinit var binding: FragmentInvestmentBinding

    private lateinit var investmentAdapter: InvestmentAdapter

    private lateinit var investmentTagAdapter: InvestmentTagAdapter

    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {

            }
        }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.getInstance(parentFragmentManager)
    }

    companion object {
        const val GRID_COUNT = 2

        const val CONTENT_COUNT = 20

        const val YOUTUBE_URI = "https://www.youtube.com/watch?v="
    }

    private val investmentViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[InvestmentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        investmentTagAdapter = InvestmentTagAdapter(object :
            InvestmentTagAdapter.InvestmentAdapterTagItemClickListener {
            override fun onItemClick(data: InvestmentTag.Data) {
                investmentAdapter.clear()
                getInvestmentList()
            }
        })

        investmentAdapter =
            InvestmentAdapter(object : InvestmentAdapter.InvestmentAdapterItemClickListener {
                override fun onItemClick(data: InvestmentInfo.Data) {
                    result.launch(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse("${YOUTUBE_URI}${data.youtubeId}")
                        )
                    )
                }
            })
        if (investmentAdapter.itemCount <= 0) {
            getInvestmentList()
        }
        if (investmentTagAdapter.itemCount <= 0) {
            getInvestmentTagList()
        }
        setupObserve()
    }

    private fun setupObserve() {
        investmentViewModel.investmentTagList.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let { list ->
                        list.add(
                            0, InvestmentTag.Data(0, context?.getString(R.string.all) ?: "all")
                        )
                        investmentTagAdapter.setAdapterList(list)
                        investmentTagAdapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {

                }

                Status.LOADING -> {

                }
            }
        }

        investmentViewModel.investmentList.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data?.data?.let { list ->
                        if (list.size < 1) {
                            context?.let { ctx ->
                                Toast.makeText(
                                    ctx, R.string.youtube_last_list, Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            investmentAdapter.setAdapterList(list)
                            investmentAdapter.notifyDataSetChanged()
                        }
                    }
                }

                Status.LOADING -> {
                    progressDialog.show()
                }

                Status.ERROR -> {
                    progressDialog.dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvestmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.investmentInfoRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.investmentTagRv.canScrollVertically(1)) {
                    investmentViewModel.investmentList.value?.data?.pagination?.let {
                        if (it.currentPage + 1 < it.totalPages) {
                            getInvestmentList()
                        }
                    }
                }
            }
        })

        binding.investmentInfoRv.adapter = investmentAdapter
        binding.investmentInfoRv.layoutManager = GridLayoutManager(context, GRID_COUNT)
        binding.investmentTagRv.adapter = investmentTagAdapter
        binding.investmentTagRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        investmentTagAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        investmentAdapter.clear()
        investmentAdapter.onDetachedFromRecyclerView(binding.investmentInfoRv)
        investmentViewModel.clearInvestmentList()
    }

    private fun getInvestmentList() {
        val page = investmentAdapter.itemCount / CONTENT_COUNT
        investmentViewModel.getInvestmentList(
            page, CONTENT_COUNT, investmentTagAdapter.selectedIndex
        )
    }

    private fun getInvestmentTagList() {
        investmentViewModel.getYoutubeTagList()
    }
}
