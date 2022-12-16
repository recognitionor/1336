package com.ham.onettsix.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.HistoryInfo
import com.ham.onettsix.databinding.RvItemMyProfileHistoryBinding

class MyProfileHistoryAdapter(
    private val context: Context,
) : RecyclerView.Adapter<MyProfileHistoryAdapter.MyProfileHistoryViewHolder>() {

    private var list: ArrayList<HistoryInfo.Data> = ArrayList()

    class MyProfileHistoryViewHolder(private val binding: RvItemMyProfileHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryInfo.Data) {

            binding.myProfileHistoryItemEpisode.text =
                itemView.resources.getString(R.string.profile_history_item_episode, item.episode)

            binding.myProfileHistoryItemPrice.text = itemView.resources.getString(
                R.string.profile_history_item_price,
                item.winningAmount
            )
            when (item.grade) {
                0 -> {
                    binding.myProfileHistoryItemStatus.setTextColor(R.color.text_black)
                    binding.myProfileHistoryItemStatus.setText(R.string.unwinnable)
                }
                1 -> {
                    binding.myProfileHistoryItemStatus.setTextColor(R.color.main_color)
                    binding.myProfileHistoryItemStatus.setText(R.string.winning)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileHistoryViewHolder {
        val binding = RvItemMyProfileHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyProfileHistoryViewHolder(binding)
    }



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
