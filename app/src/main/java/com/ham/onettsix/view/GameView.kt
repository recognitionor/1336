package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ham.onettsix.R

class GameView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    init {
        val view = inflate(context, R.layout.view_game_layout, null)
        view.apply {
        }
        addView(view)
    }
}