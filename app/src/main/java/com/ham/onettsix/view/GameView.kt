package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.ham.onettsix.R
import kotlinx.android.synthetic.main.view_game_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var clickCount = 0;

    fun start() {
        game_textview_1.text = (1..10).random().toString()
        game_textview_2.text = (1..10).random().toString()
        game_textview_3.text = (1..10).random().toString()
        AnimationUtils.loadAnimation(this.context, R.anim.anim_slot).apply {
            game_textview_1.animation = this
            game_textview_2.animation = this
            game_textview_3.animation = this
            this.start()
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                when (clickCount) {
                    0 -> {
                        game_textview_1.text = (1..10).random().toString()
                        game_textview_2.text = (1..10).random().toString()
                        game_textview_3.text = (1..10).random().toString()
                    }
                    1 -> {
                        game_textview_1.clearAnimation()
                        game_textview_2.text = (1..10).random().toString()
                        game_textview_3.text = (1..10).random().toString()
                    }
                    2 -> {
                        game_textview_2.clearAnimation()
                        game_textview_3.text = (1..10).random().toString()
                    }
                    3 -> {
                        game_textview_3.clearAnimation()
                        break
                    }
                    else -> {
                        break
                    }
                }
                delay(100)
            }
        }
    }

    init {
        val view = inflate(context, R.layout.view_game_layout, null)
        view.setOnClickListener {
            clickCount++
        }
        view.apply {
        }
        addView(view)
    }
}