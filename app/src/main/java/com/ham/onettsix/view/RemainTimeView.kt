package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ham.onettsix.R


class RemainTimeView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    init {
        val view = inflate(context, R.layout.home_remain_drawing_time, null)
        view.apply {
        }
        addView(view)
    }
}