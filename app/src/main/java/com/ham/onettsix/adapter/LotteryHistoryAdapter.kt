package com.ham.onettsix.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.LotteryHistory
import com.ham.onettsix.databinding.RvItemLotteryHistoryBinding

class LotteryHistoryAdapter :
    RecyclerView.Adapter<LotteryHistoryAdapter.LotteryHistoryViewHolder>() {

    private var list: ArrayList<LotteryHistory.Data> = ArrayList()

    class LotteryHistoryViewHolder(private val binding: RvItemLotteryHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LotteryHistory.Data) {
            binding.rvItemLotteryHistoryDayStr.text = item.endDate
            binding.rvItemLotteryHistoryWinnerUserid.text = "#${item.userId}"
            binding.rvItemLotteryHistoryTotalPrice.text = "${item.winningAmount} ${itemView.resources.getString(R.string.won)}"
            binding.rvItemLotteryHistoryEpisodeNum.text =
                itemView.resources.getString(R.string.lottery_history_count, item.episode)
            binding.rvItemLotteryHistoryWinnerName.text = item.nickname
            if (TextUtils.isEmpty(item.content)) {
                binding.rvItemLotteryHistoryComment.text = itemView.resources.getString(R.string.lottery_history_default_comment)
            } else {
                binding.rvItemLotteryHistoryComment.text = item.content
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): LotteryHistoryViewHolder {
        val binding =
            RvItemLotteryHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LotteryHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LotteryHistoryViewHolder, position: Int
    ) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setItemList(list: ArrayList<LotteryHistory.Data>) {
        this.list = list
    }
}