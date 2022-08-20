//package com.ham.onettsix.view
//
//import android.content.Context
//import android.util.AttributeSet
//import android.util.Log
//import android.view.View
//import android.widget.FrameLayout
//import com.ham.onettsix.R
//import com.ham.onettsix.constant.ResultCode
//import kotlinx.android.synthetic.main.view_game_layout.view.*
//import kotlinx.coroutines.*
//
//class RPSGameView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
//    View.OnClickListener {
//
//    interface IGameListener {
//        fun selectedGame(selectedItem: Int)
//    }
//
//    private var isStopGame = true
//    private var serverResult: Int? = null
//    private var animationDelay: Long = 50
//    private var coroutineScope: CoroutineScope? = null
//    private var selectedItem: Int = 0
//    private var selectedImage: Int = 0
//    private var gameListener: IGameListener? = null
//
//    init {
//        val view = inflate(context, R.layout.fragment_rps_game, null)
//        addView(view)
//        view.apply {
//            game_image_rock.setOnClickListener(this@RPSGameView)
//            game_image_scissors.setOnClickListener(this@RPSGameView)
//            game_image_paper.setOnClickListener(this@RPSGameView)
//            game_start_btn.setOnClickListener {
//                if (isStopGame) {
//                    game_image_rock.visibility = View.VISIBLE
//                    game_image_scissors.visibility = View.VISIBLE
//                    game_image_paper.visibility = View.VISIBLE
//                    game_start_btn.visibility = View.GONE
//                    animationDelay = 50
//                    gameStart()
//                }
//            }
//        }
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        coroutineScope?.cancel()
//    }
//
//    fun setGameListener(listener: IGameListener) {
//        this.gameListener = listener
//    }
//
//    private fun requestGame() {
//        Log.d("jhlee", "requestGame")
//        animationDelay = 200
//        if (serverResult == null) {
//            gameListener?.selectedGame(selectedItem)
//        } else {
//            serverResult?.let {
//                onGameStop(it)
//            }
//        }
//    }
//
//    override fun onClick(v: View?) {
//        when (v) {
//            game_image_rock -> {
//                if (!isStopGame) {
//                    game_image_scissors.visibility = View.INVISIBLE
//                    game_image_paper.visibility = View.INVISIBLE
//                    selectedItem = 0
//                    requestGame()
//                }
//                return
//            }
//            game_image_scissors -> {
//                if (!isStopGame) {
//                    game_image_rock.visibility = View.INVISIBLE
//                    game_image_paper.visibility = View.INVISIBLE
//                    selectedItem = 1
//                    requestGame()
//                }
//                return
//            }
//            game_image_paper -> {
//                if (!isStopGame) {
//                    game_image_rock.visibility = View.INVISIBLE
//                    game_image_scissors.visibility = View.INVISIBLE
//                    selectedItem = 2
//                    requestGame()
//                }
//                return
//            }
//        }
//    }
//
//
//    private fun gameStart() {
//        game_result_tv.visibility = View.GONE
//        game_result_tv.text = ""
//        CoroutineScope(Dispatchers.Default).launch {
//            coroutineScope = this
//            isStopGame = false
//            var count = 0
//
//            while (true) {
//                when (count % 3) {
//                    0 -> {
//                        selectedImage = R.mipmap.ic_rock
//                    }
//                    1 -> {
//                        selectedImage = R.mipmap.ic_scissors
//                    }
//                    2 -> {
//                        selectedImage = R.mipmap.ic_paper
//                    }
//                }
//                count++
//                delay(animationDelay)
//                game_image_view.post {
//                    game_image_view.setImageResource(selectedImage)
//                }
//                Log.d("jhlee", "~~~~~~~~~~~~~~~~~")
//            }
//        }
//    }
//
//    fun onGameStop(result: Int, isError: Boolean = false) {
//        val random = (0..2).random()
//        if (random == 0) {
//            // 무승부 케이스
//            coroutineScope?.cancel()
//            isStopGame = true
//            game_result_tv.visibility = View.VISIBLE
//            game_result_tv.setText(R.string.game_draw)
//            game_start_btn.visibility = View.VISIBLE
//            game_start_btn.setText(R.string.game_draw)
//            selectedImage = when (selectedItem) {
//                0 -> {
//                    R.mipmap.ic_rock
//                }
//                1 -> {
//                    R.mipmap.ic_scissors
//                }
//                else -> {
//                    R.mipmap.ic_paper
//                }
//            }
//        } else {
//            // 무승부가 아닌케이스
//            coroutineScope?.cancel()
//            isStopGame = true
//            game_start_btn.visibility = View.VISIBLE
//            when (result) {
//                ResultCode.RPC_WIN -> {
//                    game_result_tv.visibility = View.VISIBLE
//                    game_result_tv.setText(R.string.game_win)
//                    game_start_btn.setText(R.string.game_rock_scissors_paper_restart)
//                    selectedImage = when (selectedItem) {
//                        0 -> {
//                            R.mipmap.ic_scissors
//                        }
//                        1 -> {
//                            R.mipmap.ic_paper
//                        }
//                        else -> {
//                            R.mipmap.ic_rock
//                        }
//                    }
//                }
//                ResultCode.RPC_LOSE -> {
//                    game_result_tv.visibility = View.VISIBLE
//                    game_result_tv.setText(R.string.game_lose)
//                    game_start_btn.setText(R.string.game_rock_scissors_paper_restart)
//                    selectedImage = when (selectedItem) {
//                        0 -> {
//                            R.mipmap.ic_paper
//                        }
//                        1 -> {
//                            R.mipmap.ic_rock
//                        }
//                        else -> {
//                            R.mipmap.ic_scissors
//                        }
//                    }
//                }
//            }
//            serverResult = null
//        }
//        game_image_view.setImageResource(selectedImage)
//    }
//}
