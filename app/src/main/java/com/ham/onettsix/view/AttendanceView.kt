package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ham.onettsix.R
import com.ham.onettsix.data.model.GameResult
import com.ham.onettsix.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.view_attendance_layout.view.*
import kotlinx.android.synthetic.main.view_game_layout.view.*
import kotlinx.coroutines.*

class AttendanceView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    init {
        val view = inflate(context, R.layout.view_attendance_layout, null)
        addView(view)
    }
}
