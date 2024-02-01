package com.ham.onettsix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.TypingGameTag
import com.ham.onettsix.databinding.RvItemTypingGameTagItemBinding

class TypingGameTagAdapter(private val selectedChangedListener: () -> Unit) :
    RecyclerView.Adapter<TypingGameTagAdapter.TypingGameTagViewHolder>() {

    private var list: MutableList<TypingGameTag.Data> = ArrayList()

    private var selectedList: MutableList<TypingGameTag.Data> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TypingGameTagViewHolder {
        val binding = RvItemTypingGameTagItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TypingGameTagViewHolder(binding)
    }

    class TypingGameTagViewHolder(private val binding: RvItemTypingGameTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TypingGameTag.Data) {
            binding.typingGameTagTitle.text = item.tag
        }
    }

    override fun onBindViewHolder(
        holder: TypingGameTagViewHolder, position: Int
    ) {
        holder.itemView.findViewById<View>(R.id.typing_game_tag_title).apply {

            this.setOnClickListener {
                if (selectedList.contains(list[position])) {
                    selectedList.remove(list[position])
                    it.isSelected = false
                } else {
                    selectedList.add(list[position])
                    it.isSelected = true
                }
                selectedChangedListener.invoke()
            }
            this.isSelected = selectedList.contains(list[position])
        }
        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addTag(inputTag: String) {
        val data = TypingGameTag.Data(0, inputTag)
        this.list.add(0, data)
        selectedList.add(data)
    }

    fun getSelectedList(): List<String> {
        return selectedList.map {
            it.tag
        }
    }

    fun setList(myInfoList: List<TypingGameTag.Data>?) {
        myInfoList?.let {
            this.list.clear()
            this.list.addAll(myInfoList)
        }
    }
}