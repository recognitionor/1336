package com.ham.onettsix.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.HistoryInfo
import com.ham.onettsix.databinding.RvItemMyProfileHistoryBinding
import java.text.SimpleDateFormat

class MyProfileHistoryAdapter(
    private val context: Context, private val listener: OnItemWinningListener
) : RecyclerView.Adapter<MyProfileHistoryAdapter.MyProfileHistoryViewHolder>() {

    interface OnItemWinningListener {
        fun onWinnerClick(item: HistoryInfo.Data)
    }

    private var list: ArrayList<HistoryInfo.Data> = ArrayList()

    class MyProfileHistoryViewHolder(
        private val binding: RvItemMyProfileHistoryBinding,
        private val listener: OnItemWinningListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryInfo.Data) {
            binding.myProfileHistoryItemEpisode.text =
                itemView.resources.getString(R.string.profile_history_item_episode, item.episode.toString())
            binding.myProfileHistoryItemJoinInfo.text = "${item.joinCount} / ${item.totalCount}"

            binding.myProfileHistoryItemPrice.text = item.winningAmount.toString() + PreferencesHelper.getInstance(binding.root.context).getRewardUnit()
            when (item.grade) {
                0 -> {
                    binding.myProfileHistoryItemStatus.setTextColor(R.color.text_black)
                    binding.myProfileHistoryItemStatus.setText(R.string.unwinnable)
                }
                1 -> {
                    binding.root.setOnClickListener {
                        listener.onWinnerClick(item)
                    }

                    binding.myProfileHistoryItemStatus.setText(R.string.winning)
                    binding.myProfileHistoryItemStatus.setBackgroundResource(R.drawable.main_color_btn_background)
                    binding.myProfileHistoryItemStatus.setTextColor(R.color.white)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileHistoryViewHolder {
        val binding = RvItemMyProfileHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyProfileHistoryViewHolder(binding, listener)
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
