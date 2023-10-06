package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.data.model.TypingGameNormal
import com.ham.onettsix.databinding.RvItemMyProfileHistoryBinding
import com.ham.onettsix.databinding.RvItemTypingGameNormalBinding

class TypingGameNormalAdapter :
    RecyclerView.Adapter<TypingGameNormalAdapter.TypingGameNormalViewHolder>() {

    private var list: ArrayList<TypingGameNormal> = ArrayList<TypingGameNormal>().apply {
        this.add(TypingGameNormal("울트라맨#1", 2, "다시 일상으로"))
        this.add(TypingGameNormal("코끼리#2", 11, "킹인더월드"))
        this.add(TypingGameNormal("독수리#3", 23, "잭..잭..헬미..헬프미..섬바리헬미"))
        this.add(TypingGameNormal("안세영#2", 7, "겨울에 잘려보기에는 처음이잖아요"))
        this.add(TypingGameNormal("독사#4", 19, "오르막길 고바시가 세네요"))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TypingGameNormalViewHolder {
        val binding = RvItemTypingGameNormalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TypingGameNormalViewHolder(binding)
    }

    class TypingGameNormalViewHolder(private val binding: RvItemTypingGameNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TypingGameNormal) {
            binding.typingNormalItemUserTv.text = item.creator
            binding.typingNormalAttendCountTv.text = item.attendCount.toString()
            binding.typingNormalTitleTv.text = item.title
        }
    }

    override fun onBindViewHolder(
        holder: TypingGameNormalViewHolder, position: Int
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}