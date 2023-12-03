package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.TypingGameMyInfo
import com.ham.onettsix.data.model.TypingGameRankMain
import com.ham.onettsix.databinding.BottomPopupLayoutBinding
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.TimeUtils

class BottomPopupAlert(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {

    private var dismissListener: (() -> Unit)? = null
    private var myInfoList: List<TypingGameMyInfo.Data>? = null
    private var rankGameInfo: TypingGameRankMain.Data? = null
    private var userInfo: DBUser? = null

    fun setUserInfo(item: DBUser) {
        userInfo = item
        this.visibility = VISIBLE
        item.nickName?.let {
            binding.bottomImage.setImageDrawable(
                context.getDrawable(
                    ProfileImageUtil.getImageId(
                        it
                    )
                )
            )
            binding.bottomUserTv.text = "${it}#${item.uid ?: 0}"
        }
        binding.bottomUserRecord.text = "참석한 이력이 없습니다."
        binding.bottomIndexTv.visibility = View.GONE
    }

    fun setDismissListener(listener: (() -> Unit)?) {
        this.dismissListener = listener
    }

    fun dismiss() {
        this.visibility = GONE
    }

    fun setMyInfoListData(data: List<TypingGameMyInfo.Data>?) {
        this.myInfoList = data
        update()
    }

    private fun update() {
        myInfoList?.forEach {
            if (it.questionId == rankGameInfo?.questionId) {
                binding.bottomUserRecord.text = TimeUtils.getSecondString(it.duration) + "초"
                return
            }
        }
    }

    fun setRankGameData(data: TypingGameRankMain.Data?) {
        rankGameInfo = data
        update()
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