package com.ham.onettsix.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.HistoryInfo
import kotlinx.android.synthetic.main.rv_item_my_profile_history.view.*

class MyProfileHistoryAdapter(
    private val context: Context,
) : RecyclerView.Adapter<MyProfileHistoryAdapter.MyProfileHistoryViewHolder>() {

    private var list: ArrayList<HistoryInfo.Data> = ArrayList()

    class MyProfileHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: HistoryInfo.Data) {
            Log.d("jhlee", "bind ${item}")
            itemView.my_profile_history_item_episode.text =
                itemView.resources.getString(R.string.profile_history_item_episode, item.episode)
            itemView.my_profile_history_item_price.text = itemView.resources.getString(
                R.string.profile_history_item_price,
                item.winningAmount
            )
            when (item.grade) {
                0 -> {
                    itemView.my_profile_history_item_status.setTextColor(R.color.text_black)
                    itemView.my_profile_history_item_status.setText(R.string.unwinnable)
                }
                1 -> {
                    itemView.my_profile_history_item_status.setTextColor(R.color.main_color)
                    itemView.my_profile_history_item_status.setText(R.string.winning)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileHistoryViewHolder =
        MyProfileHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_my_profile_history, parent, false)
        )

    override fun onBindViewHolder(holder: MyProfileHistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: ArrayList<HistoryInfo.Data>) {
        this.list.clear()
        this.list.addAll(list)
    }
}
