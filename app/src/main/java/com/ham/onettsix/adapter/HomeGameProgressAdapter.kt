package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.EpisodeList
import com.ham.onettsix.databinding.HomeGameProgressViewBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HomeGameProgressAdapter :
    RecyclerView.Adapter<HomeGameProgressAdapter.HomeGameProgressViewHolder>() {

    private var list: ArrayList<EpisodeList.Data> = ArrayList()

    class HomeGameProgressViewHolder(private val binding: HomeGameProgressViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EpisodeList.Data) {
            when (item.episodeStatus) {
                1 -> {
                    binding.homeGameProgressCurrentTv.rotation = -45.0f
                    binding.homeGameProgressCurrentTv.visibility = View.VISIBLE
                }
                2 -> {
                    binding.homeGameProgressCurrentTv.visibility = View.INVISIBLE
                }
            }
            if (item.isEnd) {
                binding.homeGameProgressEndData.visibility = View.VISIBLE
                val date = SimpleDateFormat("yy년MM월dd일", Locale.ENGLISH).format(item.endDate)
                binding.homeGameProgressEndData.text =
                    binding.root.context.getString(R.string.home_game_progress_end_date, date)
            } else {
                binding.homeGameProgressEndData.visibility = View.INVISIBLE
            }

            binding.homeGameProgressStatus.text = item.status
            binding.homeGameProgressItemLayout.isSelected = item.episodeStatus == 1
            binding.homeGameProgressCurrentTv.isVisible = item.episodeStatus == 1
            binding.homeGameProgressEpisode.text = binding.root.context.getString(
                R.string.home_game_progress_title, item.episode.toString()
            )

            binding.homeGameProgressWinningAmount.text = binding.root.context.getString(
                R.string.home_game_progress_winning_amount, item.winningAmount
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGameProgressViewHolder {
        val binding = HomeGameProgressViewBinding.inflate(LayoutInflater.from(parent.context))
        return HomeGameProgressViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    fun setItemList(list: ArrayList<EpisodeList.Data>) {
        this.list = list
    }

    override fun onBindViewHolder(holder: HomeGameProgressViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getCurrentGamePosition(): Int {
        repeat(list.size) {
            val item = list[it]
            if (item.episodeStatus != 2) {
                return it
            }
        }
        return 0
    }
}