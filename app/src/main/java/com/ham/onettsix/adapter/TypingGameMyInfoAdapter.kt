package com.ham.onettsix.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.TypingGameMyInfo
import com.ham.onettsix.databinding.RvItemTypingGameMyinfoItemBinding
import com.ham.onettsix.utils.TimeUtils
import com.ham.onettsix.viewmodel.TypingNormalViewModel.Companion.KEY_GAME_TYPE_R

class TypingGameMyInfoAdapter :
    RecyclerView.Adapter<TypingGameMyInfoAdapter.TypingGameMyInfoViewHolder>() {

    private var list: List<TypingGameMyInfo.Data> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TypingGameMyInfoViewHolder {
        val binding = RvItemTypingGameMyinfoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TypingGameMyInfoViewHolder(binding)
    }

    class TypingGameMyInfoViewHolder(private val binding: RvItemTypingGameMyinfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TypingGameMyInfo.Data, position: Int) {
            binding.typingGameMyInfoContent.text = item.content
            binding.typingGameMyInfoRecord.text = TimeUtils.getSecondString(item.duration) + "초"
            binding.typingGameMyInfoTypeTv.text =
                if (item.gameType == KEY_GAME_TYPE_R) "랭킹게임" else "연습게임"
            binding.typingGameMyInfoStatus.text = "${item.gameOrder}등"
            val drawable: Drawable? = if (item.gameOrder == 1) {
                binding.root.context.getDrawable(R.drawable.myinfo_record_top_background)
            } else if (item.gameOrder == 2) {
                binding.root.context.getDrawable(R.drawable.myinfo_record_second_background)
            } else if (item.gameOrder < 100) {
                binding.root.context.getDrawable(R.drawable.myinfo_record_normal_background)
            } else {
                binding.typingGameMyInfoStatus.visibility = View.GONE
                null
            }
            drawable?.let {
                binding.typingGameMyInfoStatus.background = it
            }

        }
    }

    override fun onBindViewHolder(
        holder: TypingGameMyInfoViewHolder, position: Int
    ) {
        holder.itemView.setOnClickListener {}
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(myInfoList: List<TypingGameMyInfo.Data>?) {
        myInfoList?.let {
            this.list = it
        }
    }
}