package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.data.model.TypingGameItem
import com.ham.onettsix.data.model.TypingGameList
import com.ham.onettsix.databinding.RvItemTypingGameNormalBinding

class TypingGameNormalAdapter(private val listener: OnItemClickListener<TypingGameItem.Data>) :
    RecyclerView.Adapter<TypingGameNormalAdapter.TypingGameNormalViewHolder>() {

    private var list: List<TypingGameItem.Data> = ArrayList()

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
        fun bind(item: TypingGameItem.Data) {
            binding.typingNormalItemUserTv.text = "${item.nickname}#${item.userId}"
            binding.typingNormalAttendCountTv.text = item.totalJoinCnt.toString()
            binding.typingNormalTitleTv.text = item.content
        }
    }

    override fun onBindViewHolder(
        holder: TypingGameNormalViewHolder, position: Int
    ) {
        holder.itemView.setOnClickListener {
            listener.onItemClick(list[position], position)
        }
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<TypingGameItem.Data>) {
        this.list = list
    }
}