package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ham.onettsix.data.model.TypingGameRankMain
import com.ham.onettsix.databinding.BottomPopupLayoutBinding
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.TimeUtils

class BottomPopupAlert(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {

    private var dismissListener: (() -> Unit)? = null
    fun show(item: TypingGameRankMain.Data.TypingGameHistoryResItem, index: Int) {
        this.visibility = VISIBLE
        binding.bottomImage.setImageDrawable(context.getDrawable(ProfileImageUtil.getImageId(item.nickname)))
        binding.bottomUserTv.text = "${item.nickname}#${item.userId}"
        binding.bottomUserRecord.text = TimeUtils.getSecondString(item.duration) + "초"
        binding.bottomIndexTv.text = index.toString()
    }

    fun setDismissListener(listener: (() -> Unit)?) {
        this.dismissListener = listener
    }

    fun dismiss() {
        this.visibility = GONE
    }

    private var binding: BottomPopupLayoutBinding =
        BottomPopupLayoutBinding.inflate(LayoutInflater.from(context))

    init {
        binding.bottomClose.setOnClickListener {
            dismissListener?.invoke()
            dismiss()
        }
        addView(binding.root)
    }
}