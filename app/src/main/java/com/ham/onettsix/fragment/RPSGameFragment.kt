package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.RPSGameViewModel
import kotlinx.android.synthetic.main.fragment_rps_game.*
import kotlinx.android.synthetic.main.fragment_rps_game.layout_game_needed_login
import kotlinx.android.synthetic.main.layout_needed_login.*
import kotlinx.coroutines.*

class RPSGameFragment : Fragment(R.layout.fragment_rps_game), View.OnClickListener {

    val rpsGameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[RPSGameViewModel::class.java]
    }

    private var isStopGame = true
    private var serverResult: Int? = null
    private var animationDelay: Long = 50
    private var coroutineScope: CoroutineScope? = null
    private var selectedItem: Int = 0
    private var selectedImage: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserve()
        rpsGameViewModel.updateUserInfo()

        game_image_rock.setOnClickListener(this@RPSGameFragment)
        game_image_scissors.setOnClickListener(this@RPSGameFragment)
        game_image_paper.setOnClickListener(this@RPSGameFragment)
        game_start_btn.setOnClickListener(this@RPSGameFragment)
        game_info_message_img.setOnClickListener(this@RPSGameFragment)
        rps_game_login_btn.setOnClickListener(this@RPSGameFragment)

    }

    private fun setupObserve() {
        rpsGameViewModel.userInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    layout_game_needed_login.visibility = View.GONE
                }
                else -> {
                    layout_game_needed_login.visibility = View.VISIBLE
                }
            }
        }

        rpsGameViewModel.gameResult.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.SUCCESS -> {
                    it.data?.resultCode?.let { resultCode ->
                        onGameStop(resultCode)
                    }
                }
                Status.ERROR -> {
                    it.data?.resultCode?.let { resultCode ->
                        onGameStop(resultCode)
                    }
                }
                else -> {}
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rps_game, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope?.cancel()
        viewModelStore.clear()
        rpsGameViewModel.gameResult.removeObservers(viewLifecycleOwner)
    }

    private fun requestGame() {
        animationDelay = 200
        if (serverResult == null) {
            rpsGameViewModel.getRockPaperScissors()
        } else {
            serverResult?.let {
                onGameStop(it)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            rps_game_login_btn -> {
                (parentFragment as GameFragment).login()
            }
            game_info_message_img -> {
                rpsGameViewModel.getRockPaperScissors()
            }
            game_image_rock -> {
                if (!isStopGame) {
                    game_image_scissors.visibility = View.INVISIBLE
                    game_image_paper.visibility = View.INVISIBLE
                    selectedItem = 0
                    requestGame()
                }
                return
            }
            game_image_scissors -> {
                if (!isStopGame) {
                    game_image_rock.visibility = View.INVISIBLE
                    game_image_paper.visibility = View.INVISIBLE
                    selectedItem = 1
                    requestGame()
                }
                return
            }
            game_image_paper -> {
                if (!isStopGame) {
                    game_image_rock.visibility = View.INVISIBLE
                    game_image_scissors.visibility = View.INVISIBLE
                    selectedItem = 2
                    requestGame()
                }
                return
            }
            game_start_btn -> {
                if (isStopGame) {
                    game_image_rock.visibility = View.VISIBLE
                    game_image_scissors.visibility = View.VISIBLE
                    game_image_paper.visibility = View.VISIBLE
                    game_start_btn.visibility = View.GONE
                    animationDelay = 50
                    gameStart()
                }
            }
        }
    }


    private fun gameStart() {
        game_result_tv.visibility = View.GONE
        game_result_tv.text = ""
        CoroutineScope(Dispatchers.Default).launch {
            coroutineScope = this
            isStopGame = false
            var count = 0

            while (true) {
                selectedImage = when (count % 3) {
                    0 -> {
                        R.mipmap.ic_rock
                    }
                    1 -> {
                        R.mipmap.ic_scissors
                    }
                    else -> {
                        R.mipmap.ic_paper
                    }
                }
                count++
                delay(animationDelay)
                game_image_view?.post {
                    game_image_view?.setImageResource(selectedImage)
                }
            }
        }
    }

    fun loginUpdate() {
        layout_game_needed_login.visibility = View.GONE
        (requireParentFragment() as GameFragment).updateMyTicket()
        (this@RPSGameFragment.activity as MainActivity).mainViewModel.updateUserInfo()
    }

    private fun onGameStop(result: Int, isError: Boolean = false) {
        (parentFragment as GameFragment).updateMyTicket()
        coroutineScope?.cancel()
        isStopGame = true
        game_result_tv.visibility = View.VISIBLE
        game_start_btn.visibility = View.VISIBLE
        game_start_btn.setText(R.string.game_rock_scissors_paper_restart)
        when (result) {
            ResultCode.NO_TICKET -> {
                layout_game_start.visibility = View.VISIBLE
                game_info_message_img.visibility = View.VISIBLE
                game_info_message_tv.visibility = View.VISIBLE
                game_info_message_tv.setText(R.string.no_ticket)
                game_load_progress.visibility = View.GONE
            }

            ResultCode.RPC_WIN -> {
                game_result_tv.visibility = View.VISIBLE
                game_result_tv.setText(R.string.game_win)
                game_start_btn.setText(R.string.game_rock_scissors_paper_restart)
                selectedImage = when (selectedItem) {
                    0 -> {
                        R.mipmap.ic_scissors
                    }
                    1 -> {
                        R.mipmap.ic_paper
                    }
                    else -> {
                        R.mipmap.ic_rock
                    }
                }
            }
            ResultCode.RPC_LOSE -> {
                val random = (0..1).random()
                if (random == 0) {
                    // 무승부 케이스
                    game_result_tv.setText(R.string.game_draw)
                    selectedImage = when (selectedItem) {
                        0 -> {
                            R.mipmap.ic_rock
                        }
                        1 -> {
                            R.mipmap.ic_scissors
                        }
                        else -> {
                            R.mipmap.ic_paper
                        }
                    }
                } else {
                    // 패배의 경우
                    game_result_tv.setText(R.string.game_lose)
                    selectedImage = when (selectedItem) {
                        0 -> {
                            R.mipmap.ic_paper
                        }
                        1 -> {
                            R.mipmap.ic_rock
                        }
                        else -> {
                            R.mipmap.ic_scissors
                        }
                    }
                }
            }
        }
        game_image_view.setImageResource(selectedImage)
    }

    fun updateCountText(gameCount: Int, maxCount: Int) {
        game_count_tv.text =
            "$gameCount${getString(R.string.count_divide_mark, "d")}$maxCount"
    }
}
