package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ham.onettsix.R
import com.ham.onettsix.data.model.InvestmentInfo
import com.ham.onettsix.databinding.RvItemInvestmentBinding

class InvestmentAdapter(var itemClickListener: InvestmentAdapterItemClickListener) :
    RecyclerView.Adapter<InvestmentAdapter.InvestmentViewHolder>() {

    private var list: ArrayList<InvestmentInfo.Data> = ArrayList()
    interface InvestmentAdapterItemClickListener {
        fun onItemClick(data: InvestmentInfo.Data)
    }

    class InvestmentViewHolder(private val binding: RvItemInvestmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: InvestmentInfo.Data) {
            binding.investmentTitle.text = data.title
            Glide.with(binding.root.context).load(data.thumbnailLink)
                .placeholder(R.drawable.ic_no_video)
                .into(binding.investmentThumbnailImg)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): InvestmentViewHolder {
        val binding = RvItemInvestmentBinding.inflate(LayoutInflater.from(parent.context))
        return InvestmentViewHolder(binding)
    }
    override fun onBindViewHolder(holder: InvestmentViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(list[position])
        }
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setAdapterList(list: ArrayList<InvestmentInfo.Data>) {
        this.list.addAll(list)
    }

    fun clear() {
        this.list.clear()
        this.notifyDataSetChanged()
    }
}