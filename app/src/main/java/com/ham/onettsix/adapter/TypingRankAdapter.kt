package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.local.entity.DBUser
import com.ham.onettsix.data.model.TypingGameRankMain
import com.ham.onettsix.databinding.RvItemTypingHistoryBinding
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Resource
import com.ham.onettsix.utils.TimeUtils

class TypingRankAdapter(private val itemClickListener: OnItemClickListener<TypingGameRankMain.Data.TypingGameHistoryResItem>) :
    RecyclerView.Adapter<TypingRankAdapter.TypingHistoryViewHolder>() {

    private var list: ArrayList<TypingGameRankMain.Data.TypingGameHistoryResItem> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int,
    ): TypingHistoryViewHolder {
        val binding = RvItemTypingHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TypingHistoryViewHolder(binding)
    }


    class TypingHistoryViewHolder(private val binding: RvItemTypingHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(index: Int, item: TypingGameRankMain.Data.TypingGameHistoryResItem) {
            binding.bottomImage.setImageDrawable(
                binding.root.context.getDrawable(
                    ProfileImageUtil.getImageId(
                        item.nickname
                    )
                )
            )
            binding.bottomUserTv.text = "${item.nickname}#${item.userId}"
            binding.bottomUserRecord.text = "${TimeUtils.getSecondString(item.duration)}초"
            binding.bottomAttendCount.text = binding.root.context.getString(
                R.string.typing_game_attend, item.totalJoinCnt.toString()
            )
            binding.bottomIndexTv.text = (index + 1).toString()
        }
    }

    override fun onBindViewHolder(
        holder: TypingHistoryViewHolder, position: Int,
    ) {
        holder.itemView.rootView.setOnClickListener {
            itemClickListener.onItemClick(list[position], position, it)
        }
        holder.bind(position, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setList(list: List<TypingGameRankMain.Data.TypingGameHistoryResItem>) {
        this.list.clear()
        this.list.addAll(list)
    }

    fun setMyInfo(userInfo: MutableLiveData<Resource<DBUser>>) {
        
    }
}