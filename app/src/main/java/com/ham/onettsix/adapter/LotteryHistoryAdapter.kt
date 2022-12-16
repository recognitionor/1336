package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.data.model.LotteryHistory
import com.ham.onettsix.databinding.RvItemLotteryHistoryBinding

class LotteryHistoryAdapter :
    RecyclerView.Adapter<LotteryHistoryAdapter.LotteryHistoryViewHolder>() {

    private var list: ArrayList<LotteryHistory.Data> = ArrayList()

    class LotteryHistoryViewHolder(private val binding: RvItemLotteryHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LotteryHistory.Data) {
            binding.rvItemLotteryHistoryDayStr.text = item.endDate
            binding.rvItemLotteryHistoryWinnerUserid.text = item.userId.toString()
            binding.rvItemLotteryHistoryTotalPrice.text = item.winningAmount.toString()
            binding.rvItemLotteryHistoryTotalParticipationCount.text = item.memberCount.toString()
            binding.rvItemLotteryHistoryWinnerName.text = item.nickname
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LotteryHistoryViewHolder {
        val binding = RvItemLotteryHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LotteryHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LotteryHistoryViewHolder,
        position: Int
    ) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setItemList(list: ArrayList<LotteryHistory.Data>) {
        this.list = list
    }
}