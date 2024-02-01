package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ham.onettsix.R
import com.ham.onettsix.databinding.ViewEmptyLayoutBinding

class EmptyView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private var binding: ViewEmptyLayoutBinding

    init {
        val view = inflate(context, R.layout.view_empty_layout, null)
        binding = ViewEmptyLayoutBinding.inflate(LayoutInflater.from(context))
        view.apply {
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.EmptyView, 0, 0
            ).apply {
                try {
                    binding.emptyViewMsg.text =
                        getString(R.styleable.EmptyView_empty_msg)
                } finally {
                    recycle()
                }
            }
        }
        addView(binding.root)
    }
}