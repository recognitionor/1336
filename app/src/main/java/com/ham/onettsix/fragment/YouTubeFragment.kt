package com.ham.onettsix.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.adapter.YouTubeAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.model.YouTubeInfo
import com.ham.onettsix.databinding.FragmentYoutubeBinding
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.YouTubeViewModel

class YouTubeFragment : Fragment() {

    private lateinit var binding: FragmentYoutubeBinding

    private lateinit var youtubeAdapter: YouTubeAdapter

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

    private val youtubeViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[YouTubeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        youtubeAdapter = YouTubeAdapter(object : YouTubeAdapter.YouTubeAdapterItemClickListener {
            override fun onItemClick(data: YouTubeInfo.Data) {
                result.launch(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("${YOUTUBE_URI}${data.youtubeId}")
                    )
                )
            }
        })
        if (youtubeAdapter.itemCount <= 0) {
            getYoutubeList()
        }
        setupObserve()
    }

    private fun setupObserve() {
        youtubeViewModel.youtubeList.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data?.data?.let { list ->
                        if (list.size < 1) {
                            context?.let { ctx ->
                                Toast.makeText(
                                    ctx,
                                    R.string.youtube_last_list,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            youtubeAdapter.setAdapterList(list)
                            youtubeAdapter.notifyDataSetChanged()
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
        binding = FragmentYoutubeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.youtubeInfoRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.youtubeInfoRv.canScrollVertically(1)) {
                    youtubeViewModel.youtubeList.value?.data?.pagination?.let {
                        if (it.currentPage + 1 < it.totalPages) {
                            getYoutubeList()
                        }
                    }
                }
            }
        })
        binding.youtubeInfoRv.adapter = youtubeAdapter
        binding.youtubeInfoRv.layoutManager = GridLayoutManager(context, GRID_COUNT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        youtubeAdapter.clear()
        youtubeAdapter.onDetachedFromRecyclerView(binding.youtubeInfoRv)
        youtubeViewModel.clearYoutubeList()
    }

    private fun getYoutubeList() {
        val page = youtubeAdapter.itemCount / CONTENT_COUNT
        youtubeViewModel.getYoutubeList(page, CONTENT_COUNT)
    }
}
