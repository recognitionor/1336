package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.ham.onettsix.R
import com.ham.onettsix.data.model.GameResult
import com.ham.onettsix.fragment.GameFragment
import com.ham.onettsix.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.view_game_layout.view.*
import kotlinx.coroutines.*

class GameView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
    View.OnClickListener {


    private var isStopGame = true
    private var coroutineScope: CoroutineScope? = null
    private var gameViewModel: GameViewModel? = null
    private var selectedItem: Int = 0

    fun start() {

        CoroutineScope(Dispatchers.Default).launch {
            coroutineScope = this
            isStopGame = false
            var count = 0;

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
                count++;
                delay(100)
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
                    start()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            game_image_rock -> {
                selectedItem = 0
                gameViewModel?.getRockPaperScissors()
                game_image_paper.visibility = View.INVISIBLE
                game_image_scissors.visibility = View.INVISIBLE
                return
            }
            game_image_scissors -> {
                selectedItem = 1
                gameViewModel?.getRockPaperScissors()
                game_image_paper.visibility = View.INVISIBLE
                game_image_rock.visibility = View.INVISIBLE
                return
            }
            game_image_paper -> {
                selectedItem = 2
                gameViewModel?.getRockPaperScissors()
                game_image_scissors.visibility = View.INVISIBLE
                game_image_rock.visibility = View.INVISIBLE
                return
            }
        }
    }

    fun setGameViewModel(gameViewModel: GameViewModel) {
        this.gameViewModel = gameViewModel
    }

    fun onGameStop(result: GameResult?, isError: Boolean = false) {
        coroutineScope?.cancel()
        isStopGame = true
        game_start_btn.visibility = View.VISIBLE
    }
}
