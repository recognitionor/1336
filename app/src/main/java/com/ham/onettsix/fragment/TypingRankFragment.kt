package com.ham.onettsix.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.R
import com.ham.onettsix.TypingGameActivity
import com.ham.onettsix.TypingGameInfoActivity
import com.ham.onettsix.adapter.OnItemClickListener
import com.ham.onettsix.adapter.TypingRankAdapter
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.TypingGameRankMain
import com.ham.onettsix.databinding.FragmentTypingRankBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingRankViewModel


class TypingRankFragment : Fragment() {

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            typingRankViewModel.getRankMain()
        }

    private val typingRankViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext())),
                PreferencesHelper.getInstance(requireContext())
            )
        )[TypingRankViewModel::class.java]
    }

    private lateinit var typingRankAdapter: TypingRankAdapter

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
        binding.typingHistoryRv.adapter = typingRankAdapter
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typingRankAdapter = TypingRankAdapter(object :
            OnItemClickListener<TypingGameRankMain.Data.TypingGameHistoryResItem> {
            override fun onItemClick(
                item: TypingGameRankMain.Data.TypingGameHistoryResItem, index: Int, view: View?
            ) {
                binding.myInfoRecord.visibility = View.GONE
                binding.typingBottomAlert.show(item, index)
            }
        })


        setupObserver()

    }

    private fun setupObserver() {
        typingRankViewModel.rankGame.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.typingGameRankProgressLayout.visibility = View.GONE
                    binding.typingGameRankEmptyLayout.visibility = View.GONE
                    result.data?.let { rankGameData ->
                        val currentTimeInMillis = System.currentTimeMillis()
                        val timeDifferenceInMillis = rankGameData.endDate - currentTimeInMillis

                        val seconds = timeDifferenceInMillis / 1000
                        val minutes = seconds / 60
                        val hours = minutes / 60
                        val days = hours / 24

                        val remainingHours = hours % 24
                        val remainingMinutes = minutes % 60
                        binding.typingGameTime.text = "${days}일${remainingHours}시간 남음"
                        binding.typingGameRankContent.text = rankGameData.content
                        binding.typingGameHonoraryMember.text = resources.getString(
                            R.string.typing_game_honorary_member,
                            rankGameData.typingGameHistoryResList.size.toString()
                        )
                        binding.typingGameTotalChallenger.text = resources.getString(
                            R.string.typing_game_total_challenger,
                            rankGameData.totalJoinUserCount.toString()
                        )
                        typingRankAdapter.setList(rankGameData.typingGameHistoryResList)
                        typingRankAdapter.notifyDataSetChanged()
                    }
                }

                Status.LOADING -> {
                    binding.typingGameRankProgressLayout.visibility = View.VISIBLE
                    binding.typingGameRankEmptyLayout.visibility = View.GONE
                }

                Status.ERROR -> {
                    binding.typingGameRankProgressLayout.visibility = View.GONE
                    binding.typingGameRankEmptyLayout.visibility = View.VISIBLE
                }
            }
        }
        typingRankViewModel.selectedTypingGame.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                }

                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
        typingRankViewModel.typingGameList.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                }

                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typingHistoryRv.layoutManager = LinearLayoutManager(context)
        binding.typingHistoryRv.adapter = typingRankAdapter
        binding.typingGameRankLayout.setOnClickListener {
            val intent = Intent(
                requireContext(), TypingGameActivity::class.java
            )
            val rankMainData = typingRankViewModel.rankGame.value?.data
            rankMainData?.let {
                intent.putExtra(ExtraKey.TYPING_GAME_CONTENT, it.content)
                intent.putExtra(ExtraKey.TYPING_GAME_QUESTION_ID, it.questionId)
                intent.putExtra(ExtraKey.TYPING_GAME_EPISODE, it.episode)
                intent.putExtra(ExtraKey.TYPING_GAME_IS_RANK_GAME, true)
                startForResult.launch(intent)
            }
        }
        binding.typingBottomAlert.setDismissListener {
            binding.myInfoRecord.visibility = View.VISIBLE
        }
        binding.myInfoRecord.setOnClickListener {
            startActivity(Intent(requireContext(), TypingGameInfoActivity::class.java))
        }
        binding.typingGameRankEmptyLayout.setOnClickListener {
            typingRankViewModel.getRankMain()
        }
    }
}