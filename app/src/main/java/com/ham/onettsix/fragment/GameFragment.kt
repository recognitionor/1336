package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.R
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.view_attendance_layout.*
import kotlinx.android.synthetic.main.view_attendance_layout.view.*
import kotlinx.android.synthetic.main.view_game_layout.*
import kotlinx.android.synthetic.main.view_game_layout.view.*

class GameFragment : Fragment() {

    var slotTv1: Int = 0;
    var slotTv2: Int = 0;
    var slotTv3: Int = 0;

    private val gameViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[GameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()

        gameViewModel.validateLimitedRv()
//        gameViewModel.getVideoSignature()
//        gameViewModel.gameLoad()
        gameViewModel.attendLoad()
    }

    private fun setupObserve() {
        gameViewModel.validateLimitedRvStatus.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {}
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }

        gameViewModel.videoSignature.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {}
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }

        gameViewModel.attendStatus.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.resultCode == ResultCode.DUPLICATED_ATTEND) {
                        attendance_btn.isEnabled = false
                        attendance_btn.text = it.data.description
                    } else {
                        attendance_btn.isEnabled = true
                        attendance_btn.setText(R.string.attend_btn)
                    }
                    attendance_progress.visibility = View.GONE
                    attendance_btn.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    attendance_progress.visibility = View.VISIBLE
                    attendance_btn.visibility = View.GONE
                }
                Status.ERROR -> {
                    attendance_progress.visibility = View.GONE
                    attendance_btn.visibility = View.GONE
                }
            }
        }
        gameViewModel.gameTypeInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val gameCount = it.data?.data?.gameCount ?: 0
                    val maxCount = it.data?.data?.maxCount ?: 0
                    game_count_tv.text =
                        "$gameCount${getString(R.string.count_divide_mark, "d")}$maxCount"
                    if (gameCount < maxCount) {
                        // 참여가능
                        layout_game_start.visibility = View.GONE
                        game_layout.visibility = View.VISIBLE
                        game_info_message_img.visibility = View.GONE
                        game_info_message_tv.visibility = View.GONE
                        game_load_progress.visibility = View.GONE
                    } else {
                        // 참여불가능
                        layout_game_start.visibility = View.VISIBLE
                        game_layout.visibility = View.VISIBLE
                        game_info_message_img.visibility = View.VISIBLE
                        game_info_message_tv.visibility = View.VISIBLE
                        game_load_progress.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    game_load_progress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    game_load_progress.visibility = View.GONE
                    game_info_message_img.visibility = View.VISIBLE
                    game_info_message_tv.visibility = View.VISIBLE
                }
            }
        }
        gameViewModel.gameResult.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    game_layout.onGameStop(it?.data)
                }
                Status.ERROR -> {
                    game_layout.onGameStop(it?.data)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_game, null).apply {
            attendance_btn.setOnClickListener {
                gameViewModel.attendCheck()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        game_layout.setGameViewModel(gameViewModel)
    }
}
