package com.ham.onettsix.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.R
import com.ham.onettsix.data.model.TypingGameItem
import com.ham.onettsix.data.model.TypingGameUserItem
import com.ham.onettsix.databinding.RvItemTypingGameNormalBinding
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.TimeUtils

class TypingGameNormalAdapter(
    private val listener: OnItemClickListener<TypingGameItem.Data>
) : RecyclerView.Adapter<TypingGameNormalAdapter.TypingGameNormalViewHolder>() {

    private var list: List<TypingGameItem.Data> = ArrayList()


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TypingGameNormalViewHolder {
        val binding = RvItemTypingGameNormalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        return TypingGameNormalViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    class TypingGameUserView(
        context: Context, attrs: AttributeSet?, private val userItemList: List<TypingGameUserItem>
    ) : LinearLayoutCompat(context, attrs) {


        private var itemViewList: MutableList<View?> = MutableList(3) { null }

        init {

            LayoutInflater.from(context)
                .inflate(R.layout.rv_item_typing_game_normal_user_layout, this)
            itemViewList.add(0, findViewById(R.id.rv_item_typing_game_normal_user_item_1))
            itemViewList.add(1, findViewById(R.id.rv_item_typing_game_normal_user_item_2))
            itemViewList.add(2, findViewById(R.id.rv_item_typing_game_normal_user_item_3))

            itemViewList.forEach {
                it?.visibility = View.GONE
            }
            userItemList.forEachIndexed { index, typingGameUserItem ->
                val tempView = itemViewList[index]
                itemViewList[index]?.visibility = View.VISIBLE
                tempView?.findViewById<AppCompatImageView>(R.id.typing_game_normal_user_image)
                    ?.apply {
                        this.setImageDrawable(
                            resources.getDrawable(
                                ProfileImageUtil.getImageId(typingGameUserItem.nickname), null
                            )
                        )
                    }
                tempView?.findViewById<AppCompatTextView>(R.id.typing_game_normal_user_name)
                    ?.apply {
                        this.text = "${typingGameUserItem.nickname}#${typingGameUserItem.userId}"
                    }

                tempView?.findViewById<AppCompatTextView>(R.id.typing_game_normal_user_rank_mark)
                    ?.apply {
                        this.text = "${(index + 1)} 등"
                    }
                Log.d(
                    "jhlee",
                    "typingGameUserItem.duration.toFloat() :${typingGameUserItem.duration}"
                )
                tempView?.findViewById<AppCompatTextView>(R.id.typing_game_normal_user_record)
                    ?.apply {
                        Log.d("jhlee", "${"${TimeUtils.getSecondString(typingGameUserItem.duration)}초"}")
                        this.text = "${TimeUtils.getSecondString(typingGameUserItem.duration)}초"
                    }
            }
        }
    }

    class TypingGameNormalViewHolder(private val binding: RvItemTypingGameNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TypingGameItem.Data) {
            if (item.userOrders.isEmpty()) {
                binding.userListLayout.visibility = View.GONE
            } else {
                binding.userListLayout.visibility = View.VISIBLE
                // 아이템의 레이아웃 파라미터 설정
                binding.userListLayout.addView(
                    TypingGameUserView(
                        binding.root.context, null, item.userOrders
                    )
                )
            }


            binding.typingNormalItemUserTv.text = "${item.nickname}#${item.userId}"
            binding.typingNormalAttendCountTv.text = item.totalJoinCnt.toString()
            binding.typingNormalTitleTv.text = item.content

        }
    }

    override fun onBindViewHolder(
        holder: TypingGameNormalViewHolder, position: Int
    ) {
        holder.itemView.setOnClickListener {
            listener.onItemClick(list[position], position)
        }

        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<TypingGameItem.Data>) {
        this.list = list
    }
}