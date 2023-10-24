package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ham.onettsix.data.model.TypingHistory
import com.ham.onettsix.databinding.BottomPopupLayoutBinding

class BottomPopupAlert(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {
    fun show(item: TypingHistory) {
        this.visibility = VISIBLE
        binding.bottomUserTv.text = item.user
        binding.bottomUserRecord.text = item.record.toString()
        binding.bottomIndexTv.text = item.ranking.toString()
    }

    fun dismiss() {
        this.visibility = GONE
    }

    private lateinit var binding: BottomPopupLayoutBinding

    init {
        binding = BottomPopupLayoutBinding.inflate(LayoutInflater.from(context))
        binding.bottomClose.setOnClickListener {
            dismiss()
        }
        addView(binding.root)
    }
}