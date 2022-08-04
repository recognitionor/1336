package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import com.ham.onettsix.R
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class ToolbarView(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {
    init {
        val view = inflate(context, R.layout.toolbar_layout, null)
        view.apply {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ToolbarView,
                0, 0
            ).apply {
                try {
                    toolbar_back_title_textview.text =
                        getString(R.styleable.ToolbarView_toolbar_title)
                } finally {
                    recycle()
                }
            }
        }

        addView(view)
    }
}