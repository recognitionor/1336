package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ham.onettsix.R
import com.ham.onettsix.data.model.YouTubeInfo
import com.ham.onettsix.databinding.RvItemYoutubeBinding

class YouTubeAdapter(var itemClickListener: YouTubeAdapterItemClickListener) :
    RecyclerView.Adapter<YouTubeAdapter.YouTubeViewHolder>() {

    private var list: ArrayList<YouTubeInfo.Data> = ArrayList()
    interface YouTubeAdapterItemClickListener {
        fun onItemClick(data: YouTubeInfo.Data)
    }

    class YouTubeViewHolder(private val binding: RvItemYoutubeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: YouTubeInfo.Data) {
            binding.youtubeTitle.text = data.title
            Glide.with(binding.root.context).load(data.thumbnailLink)
                .placeholder(R.drawable.ic_no_video)
                .into(binding.youtubeThumbnailImg)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): YouTubeViewHolder {
        val binding = RvItemYoutubeBinding.inflate(LayoutInflater.from(parent.context))
        return YouTubeViewHolder(binding)
    }
    override fun onBindViewHolder(holder: YouTubeViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(list[position])
        }
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setAdapterList(list: ArrayList<YouTubeInfo.Data>) {
        this.list.addAll(list)
    }

    fun clear() {
        this.list.clear()
        this.notifyDataSetChanged()
    }
}