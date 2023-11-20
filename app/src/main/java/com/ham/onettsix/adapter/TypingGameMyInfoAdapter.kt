package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.data.model.TypingGameMyInfo
import com.ham.onettsix.databinding.RvItemTypingGameMyinfoItemBinding
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
            binding.typingGameMyInfoRecord.text = item.duration.toString()
            binding.typingGameMyInfoTypeTv.text = if (item.gameType == KEY_GAME_TYPE_R) "랭킹게임" else "연습게임"
            binding.typingGameMyInfoStatus.text = "${item.gameOrder}등"
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