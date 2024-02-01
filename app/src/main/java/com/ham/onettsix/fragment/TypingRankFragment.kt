package com.ham.onettsix.fragment

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
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
import com.ham.onettsix.dialog.OneButtonDialog
import com.ham.onettsix.dialog.TwoButtonDialog
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
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
                item: TypingGameRankMain.Data.TypingGameHistoryResItem, index: Int, view: View?,
            ) {
            }
        })


        setupObserver()

    }

    private fun setupObserver() {
        typingRankViewModel.typingGameValidation.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    val joinCnt = result.data?.data?.joinCnt ?: 0
                    val maxCnt = result.data?.data?.maxCount ?: 0
                    val remainedTicket = result.data?.data?.remainedTicket ?: 0
                    if (remainedTicket < 1) {
                        val message =
                            getString(R.string.no_ticket)
                        OneButtonDialog(
                            getString(R.string.typing_game_rank_validation_title), message
                        ) {
                            it.dismiss()
                        }.show(this@TypingRankFragment.childFragmentManager, OneButtonDialog.TAG)
                        return@observe
                    }

                    if (joinCnt >= maxCnt) {
                        val message =
                            getString(R.string.typing_game_rank_validation_message_invalid)
                        OneButtonDialog(
                            getString(R.string.typing_game_rank_validation_title), message
                        ) {
                            it.dismiss()
                        }.show(this@TypingRankFragment.childFragmentManager, OneButtonDialog.TAG)
                    } else {
                        val message = getString(
                            R.string.typing_game_rank_validation_message_valid,
                            (maxCnt - joinCnt).toString()
                        )
                        TwoButtonDialog(
                            getString(R.string.typing_game_rank_validation_title), message
                        ) { isPositive, dialog ->
                            if (isPositive) {
                                val intent =
                                    Intent(requireContext(), TypingGameActivity::class.java)
                                val rankMainData = typingRankViewModel.rankGame.value?.data
                                rankMainData?.let {
                                    intent.putExtra(ExtraKey.TYPING_GAME_CONTENT, it.content)
                                    intent.putExtra(ExtraKey.TYPING_GAME_QUESTION_ID, it.questionId)
                                    intent.putExtra(ExtraKey.TYPING_GAME_EPISODE, it.episode)
                                    intent.putExtra(ExtraKey.TYPING_GAME_IS_RANK_GAME, true)
                                    startForResult.launch(intent)
                                }
                            }
                            dialog.dismiss()
                        }.show(this@TypingRankFragment.childFragmentManager, TwoButtonDialog.TAG)
                    }
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                }
            }
        }

        typingRankViewModel.userInfo.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    result.data?.let {
                        binding.typingBottomAlert.setUserInfo(it)
                    }
                }

                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
        typingRankViewModel.myTypingGameRecord.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    alertLayout()
                    binding.typingBottomAlert.setMyInfoListData(result.data)
                }

                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
        typingRankViewModel.rankGame.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.typingBottomAlert.setRankGameData(result.data)
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
                        alertLayout()
                        typingRankAdapter.setList(rankGameData.typingGameHistoryResList)
                        typingRankAdapter.setMyInfo(typingRankViewModel.userInfo)
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

    private fun alertLayout() {
        val userInfo = typingRankViewModel.userInfo.value?.data
        val gameList = typingRankViewModel.rankGame.value?.data?.typingGameHistoryResList
        var chaseUser: TypingGameRankMain.Data.TypingGameHistoryResItem?
        var myInfo: TypingGameRankMain.Data.TypingGameHistoryResItem?
        try {
            if (gameList != null && gameList.isNotEmpty() && userInfo != null) {
                gameList.forEachIndexed { index, typingGameHistoryResItem ->
                    if (typingGameHistoryResItem.userId == userInfo.uid?.toLong()) {
                        if (gameList[index + 1].userId != userInfo.uid.toLong()) {
                            myInfo = gameList[index]
                            chaseUser = gameList[index + 1]
                            if (chaseUser != null && myInfo != null) {
                                val fadeOut = AnimatorInflater.loadAnimator(
                                    this.requireContext(), R.animator.vanish
                                ) as ObjectAnimator
                                fadeOut.target = binding.chaseNotificationTv

                                // Start the animation
                                fadeOut.start()
                                binding.chaseNotificationTv.visibility = View.VISIBLE
                                binding.chaseNotificationTv.text = resources.getString(
                                    R.string.chase_notification_msg,
                                    "${chaseUser!!.nickname}#${chaseUser!!.userId}",
                                    "${chaseUser!!.duration - myInfo!!.duration}"
                                )
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typingHistoryRv.layoutManager = LinearLayoutManager(context)
        binding.typingHistoryRv.adapter = typingRankAdapter
        binding.typingGameRankLayout.setOnClickListener {
            typingRankViewModel.getTypingGameValidation()
        }
        binding.typingBottomAlert.setOnClickListener {
            startActivity(Intent(requireContext(), TypingGameInfoActivity::class.java))
        }
        binding.typingBottomAlert.setDismissListener {
            binding.myInfoRecord.visibility = View.VISIBLE
        }

        binding.typingGameRankEmptyLayout.setOnClickListener {
            typingRankViewModel.getRankMain()
        }
        binding.myInfoRecord.setOnClickListener {
            it.visibility = View.GONE
            binding.typingBottomAlert.visibility = View.VISIBLE
        }
    }
}