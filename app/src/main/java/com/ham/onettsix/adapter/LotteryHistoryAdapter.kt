package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.HistoryInfo
import com.ham.onettsix.data.model.LotteryHistory
import kotlinx.android.synthetic.main.rv_item_lottery_history.view.*

class LotteryHistoryAdapter :
    RecyclerView.Adapter<LotteryHistoryAdapter.LotteryHistoryViewHolder>() {

    private var list: ArrayList<LotteryHistory.Data> = ArrayList()

    class LotteryHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: LotteryHistory.Data) {
            itemView.rv_item_lottery_history_day_str.text = item.endDate
            itemView.rv_item_lottery_history_winner_userid.text = item.userId.toString()
            itemView.rv_item_lottery_history_total_price.text = item.winningAmount.toString()
            itemView.rv_item_lottery_history_total_participation_count.text = item.memberCount.toString()
            itemView.rv_item_lottery_history_winner_name.text = item.nickname
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LotteryHistoryViewHolder {
        return LotteryHistoryAdapter.LotteryHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_lottery_history, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: LotteryHistoryAdapter.LotteryHistoryViewHolder,
        position: Int
    ) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setItemList(list: ArrayList<LotteryHistory.Data>) {
        this.list = list
    }
}