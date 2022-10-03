package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ham.onettsix.R
import com.ham.onettsix.data.model.LotteryHistory
import com.ham.onettsix.data.model.Notice
import com.ham.onettsix.utils.TimeUtils
import kotlinx.android.synthetic.main.rv_item_lottery_history.view.*
import kotlinx.android.synthetic.main.rv_item_notice.view.*

class NoticeAdapter :
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private var list: ArrayList<Notice.Data> = ArrayList()

    class NoticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Notice.Data) {
            itemView.notice_item_title_tv.text = item.title
            itemView.notice_item_content_tv.text = item.content
            itemView.notice_item_time_tv.text = TimeUtils.timeDiff(item.noticedTime.toLong())
            Glide.with(itemView.context)
                .load(R.drawable.ic_alarm)
                .into(itemView.notice_item_icon_img)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeViewHolder {
        return NoticeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_notice, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: NoticeViewHolder,
        position: Int
    ) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setItemList(list: ArrayList<Notice.Data>) {
        this.list = list
    }
}