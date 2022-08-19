package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.ham.onettsix.R
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.model.GameResult
import com.ham.onettsix.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.view_game_layout.view.*
import kotlinx.coroutines.*
import kotlin.random.Random

class GameView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
    View.OnClickListener {

    private var isStopGame = true
    private var isServerCall = false
    private var animationDelay: Long = 50
    private var coroutineScope: CoroutineScope? = null
    private var gameViewModel: GameViewModel? = null
    private var selectedItem: Int = 0

    private fun gameStart() {
        game_result_tv.visibility = View.GONE
        game_result_tv.text = ""
        CoroutineScope(Dispatchers.Default).launch {
            coroutineScope = this
            isStopGame = false
            var count = 0

            while (true) {
                when (count % 3) {
                    0 -> {
                        game_image_view.post {
                            game_image_view.setImageResource(R.mipmap.ic_rock)
                        }
                    }
                    1 -> {
                        game_image_view.post {
                            game_image_view.setImageResource(R.mipmap.ic_scissors)
                        }
                    }
                    2 -> {
                        game_image_view.post {
                            game_image_view.setImageResource(R.mipmap.ic_paper)
                        }
                    }
                }
                count++
                delay(animationDelay)
            }
        }
    }

    init {
        val view = inflate(context, R.layout.view_game_layout, null)
        addView(view)
        view.apply {
            game_image_rock.setOnClickListener(this@GameView)
            game_image_scissors.setOnClickListener(this@GameView)
            game_image_paper.setOnClickListener(this@GameView)
            game_start_btn.setOnClickListener {
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

    private fun requestGame() {
        animationDelay = 200
        if (!isServerCall) {
            gameViewModel?.getRockPaperScissors()
        }
        gameViewModel?.getRockPaperScissors()
        game_image_paper.visibility = View.INVISIBLE
        game_image_scissors.visibility = View.INVISIBLE
    }

    override fun onClick(v: View?) {
        when (v) {
            game_image_rock -> {
                if (!isStopGame) {
                    selectedItem = 0
                    requestGame()
                }
                return
            }
            game_image_scissors -> {
                if (!isStopGame) {
                    selectedItem = 1
                    requestGame()
                }
                return
            }
            game_image_paper -> {
                if (!isStopGame) {
                    selectedItem = 2
                    requestGame()
                }
                return
            }
        }
    }

    fun setGameViewModel(gameViewModel: GameViewModel) {
        this.gameViewModel = gameViewModel
    }

    fun onGameStop(result: GameResult?, isError: Boolean = false) {
        Log.d("jhlee", "onGameStop : $result")
        isServerCall = true
        val random = (0..2).random()
        if (random == 0) {
            // 무승부 케이스
            game_result_tv.visibility = View.VISIBLE
            game_result_tv.setText(R.string.game_draw)
            game_image_rock.visibility = View.VISIBLE
            game_image_paper.visibility = View.VISIBLE
            game_image_scissors.visibility = View.VISIBLE
            coroutineScope?.cancel()
            isStopGame = true
            game_start_btn.visibility = View.VISIBLE
            when (selectedItem) {
                0 -> {
                    game_image_view.setImageResource(R.mipmap.ic_rock)
                }
                1 -> {
                    game_image_view.setImageResource(R.mipmap.ic_scissors)
                }
                2 -> {
                    game_image_view.setImageResource(R.mipmap.ic_paper)
                }
            }
        } else {
            // 무승부가 아닌케이스
            coroutineScope?.cancel()
            isStopGame = true
            game_start_btn.visibility = View.VISIBLE
            when (result?.resultCode) {
                ResultCode.RPC_WIN -> {
                    game_result_tv.visibility = View.VISIBLE
                    game_result_tv.setText(R.string.game_win)
                    when (selectedItem) {
                        0 -> {
                            game_image_view.setImageResource(R.mipmap.ic_paper)
                        }
                        1 -> {
                            game_image_view.setImageResource(R.mipmap.ic_rock)
                        }
                        2 -> {
                            game_image_view.setImageResource(R.mipmap.ic_scissors)
                        }
                    }
                    return
                }
                ResultCode.RPC_LOSE -> {
                    game_result_tv.visibility = View.VISIBLE
                    game_result_tv.setText(R.string.game_lose)
                    when (selectedItem) {
                        0 -> {
                            game_image_view.setImageResource(R.mipmap.ic_scissors)
                        }
                        1 -> {
                            game_image_view.setImageResource(R.mipmap.ic_paper)
                        }
                        2 -> {
                            game_image_view.setImageResource(R.mipmap.ic_rock)
                        }
                    }
                    return
                }
            }
        }


        coroutineScope?.cancel()
        isStopGame = true
        game_start_btn.visibility = View.VISIBLE
    }
}
