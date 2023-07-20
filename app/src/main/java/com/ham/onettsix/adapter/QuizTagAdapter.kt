package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ham.onettsix.R
import com.ham.onettsix.data.model.Notice
import com.ham.onettsix.data.model.QuizTag
import com.ham.onettsix.databinding.RvItemNoticeBinding
import com.ham.onettsix.databinding.RvItemQuizTagBinding
import com.ham.onettsix.utils.TimeUtils

class QuizTagAdapter : RecyclerView.Adapter<QuizTagAdapter.QuizTagViewHolder>() {

    private var list: ArrayList<QuizTag.Data> = ArrayList()

    class QuizTagViewHolder(private val binding: RvItemQuizTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuizTag.Data) {
            binding.rvItemQuizTagName.text = item.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): QuizTagViewHolder {
        val binding = RvItemQuizTagBinding.inflate(LayoutInflater.from(parent.context))
        return QuizTagViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: QuizTagViewHolder, position: Int
    ) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setItemList(list: ArrayList<QuizTag.Data>) {
        this.list = list
    }
}