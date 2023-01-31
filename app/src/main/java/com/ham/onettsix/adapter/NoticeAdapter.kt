package com.ham.onettsix.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ham.onettsix.R
import com.ham.onettsix.data.model.Notice
import com.ham.onettsix.databinding.RvItemNoticeBinding
import com.ham.onettsix.utils.TimeUtils

class NoticeAdapter : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private var list: ArrayList<Notice.Data> = ArrayList()

    class NoticeViewHolder(private val binding: RvItemNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Notice.Data) {
            binding.noticeItemTitleTv.text = item.title
            binding.noticeItemContentTv.text = item.content
            binding.noticeItemTimeTv.text =
                TimeUtils.timeDiff(itemView.context, item.createdAt.toLong())
            Glide.with(itemView.context).load(R.drawable.ic_alarm).into(binding.noticeItemIconImg)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): NoticeViewHolder {
        val binding = RvItemNoticeBinding.inflate(LayoutInflater.from(parent.context))
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoticeViewHolder, position: Int
    ) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setItemList(list: ArrayList<Notice.Data>) {
        this.list = list
    }
}