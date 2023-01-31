package com.ham.onettsix.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.data.model.InvestmentTag
import com.ham.onettsix.databinding.RvItemInvestmentTagBinding

class InvestmentTagAdapter(var itemClickListener: InvestmentAdapterTagItemClickListener) :
    RecyclerView.Adapter<InvestmentTagAdapter.InvestmentTagViewHolder>() {

    private var list = ArrayList<InvestmentTag.Data>()

    var selectedIndex: Int = 0

    interface InvestmentAdapterTagItemClickListener {
        fun onItemClick(data: InvestmentTag.Data)
    }

    class InvestmentTagViewHolder(private val binding: RvItemInvestmentTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: InvestmentTag.Data, isSelected: Boolean, listener: OnClickListener) {
            binding.investmentTagRvItemCheckbox.text = data.name
            binding.investmentTagRvItemCheckbox.isSelected = isSelected
            binding.investmentTagRvItemCheckbox.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): InvestmentTagViewHolder {
        val binding = RvItemInvestmentTagBinding.inflate(LayoutInflater.from(parent.context))
        return InvestmentTagViewHolder(binding)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: InvestmentTagViewHolder, position: Int) {
        holder.bind(list[position], position == selectedIndex) {
            val oldIndex = selectedIndex
            this.selectedIndex = position
            itemClickListener.onItemClick(list[position])
            notifyItemChanged(oldIndex)
            notifyItemChanged(selectedIndex)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setAdapterList(list: ArrayList<InvestmentTag.Data>) {
        this.list = list
    }

    fun clear() {
        this.list.clear()
        this.notifyDataSetChanged()
    }
}