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
import com.ham.onettsix.viewmodel.RPSGameViewModel
import kotlinx.android.synthetic.main.fragment_rps_game.*
import kotlinx.coroutines.*

class RPSGameFragment : Fragment(),
    View.OnClickListener {

    private val rpsGameViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(
            ApiHelperImpl(RetrofitBuilder.apiService),
            DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
        ))[RPSGameViewModel::class.java]
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
        rpsGameViewModel.gameLoad()
        game_image_rock.setOnClickListener(this@RPSGameFragment)
        game_image_scissors.setOnClickListener(this@RPSGameFragment)
        game_image_paper.setOnClickListener(this@RPSGameFragment)
        game_start_btn.setOnClickListener(this@RPSGameFragment)
        game_info_message_img.setOnClickListener(this@RPSGameFragment)

    }

    private fun setupObserve() {
        rpsGameViewModel.gameResult.observe(this) {
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
            }
        }

        rpsGameViewModel.gameTypeInfo.observe(this) {
            when (it?.status) {
                Status.SUCCESS -> {
                    val gameCount = it.data?.data?.gameCount ?: 0
                    val maxCount = it.data?.data?.maxCount ?: 0
                    game_count_tv.text =
                        "$gameCount${getString(R.string.count_divide_mark, "d")}$maxCount"
                    if (gameCount < maxCount) {
                        // 참여가능
                        layout_game_start.visibility = View.GONE
                        game_info_message_img.visibility = View.GONE
                        game_info_message_tv.visibility = View.GONE
                        game_load_progress.visibility = View.GONE
                    } else {
                        // 참여불가능
                        layout_game_start.visibility = View.VISIBLE
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rps_game, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope?.cancel()

//        rpsGameViewModel.gameResult.value = Resource.success(null)
        rpsGameViewModel.gameResult.removeObservers(this)
        rpsGameViewModel.gameTypeInfo.removeObservers(this)
        rpsGameViewModel.gameResult.removeObservers(viewLifecycleOwner)
        rpsGameViewModel.gameTypeInfo.removeObservers(viewLifecycleOwner)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
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

    private fun onGameStop(result: Int, isError: Boolean = false) {
        val random = (0..2).random()
        if (random == 0) {
            // 무승부 케이스
            coroutineScope?.cancel()
            isStopGame = true
            game_result_tv.visibility = View.VISIBLE
            game_result_tv.setText(R.string.game_draw)
            game_start_btn.visibility = View.VISIBLE
            game_start_btn.setText(R.string.game_draw)
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
            // 무승부가 아닌케이스
            coroutineScope?.cancel()
            isStopGame = true
            game_start_btn.visibility = View.VISIBLE
            when (result) {
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
                    game_result_tv.visibility = View.VISIBLE
                    game_result_tv.setText(R.string.game_lose)
                    game_start_btn.setText(R.string.game_rock_scissors_paper_restart)
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
            serverResult = null
        }
        game_image_view.setImageResource(selectedImage)
    }
}
