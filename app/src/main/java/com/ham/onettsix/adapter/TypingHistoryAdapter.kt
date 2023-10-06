package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.TypingHistory
import com.ham.onettsix.databinding.RvItemTypingHistoryBinding

class TypingHistoryAdapter : RecyclerView.Adapter<TypingHistoryAdapter.TypingHistoryViewHolder>() {

    private var list: ArrayList<TypingHistory> = ArrayList<TypingHistory>().apply {
        this.add(TypingHistory(1, "에이미림", 1f, 1))
        this.add(TypingHistory(2, "자우림", 11f, 11))
        this.add(TypingHistory(3, "피터림", 32f, 5))
        this.add(TypingHistory(4, "열대우림", 233f, 4))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TypingHistoryViewHolder {
        val binding = RvItemTypingHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TypingHistoryViewHolder(binding)
    }

    class TypingHistoryViewHolder(private val binding: RvItemTypingHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TypingHistory) {
            binding.bottomUserTv.text = item.user
            binding.bottomUserRecord.text = item.record.toString()
            binding.bottomAttendCount.text =
                binding.root.context.getString(R.string.typing_game_attend, item.count.toString())
            binding.bottomIndexTv.text = item.ranking.toString()
        }
    }

    override fun onBindViewHolder(
        holder: TypingHistoryViewHolder, position: Int
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}