package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ham.onettsix.databinding.BottomPopupLayoutBinding

class BottomPopupAlert(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {
    private lateinit var binding: BottomPopupLayoutBinding
    init {
        binding = BottomPopupLayoutBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }
}