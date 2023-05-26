package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ham.onettsix.R
import com.ham.onettsix.databinding.HomeGameHelpLayoutBinding
import com.ham.onettsix.databinding.HomeGameProgressViewBinding

class HomeGameHelpLayout(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {

    private lateinit var binding: HomeGameHelpLayoutBinding

    init {
        Log.d("jhlee", "HomeGameHelpLayout")
        binding = HomeGameHelpLayoutBinding.inflate(LayoutInflater.from(context))
        val view = binding.root
        view.apply {
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.HomeGameHelpLayout, 0, 0
            ).apply {
                try {
                    binding.homeGameHelpInfoCount.setImageDrawable(getDrawable(R.styleable.HomeGameHelpLayout_home_game_help_count))
                    binding.homeGameHelpInfoTitle.text =
                        getString(R.styleable.HomeGameHelpLayout_home_game_help_title)
                    binding.homeHelpInfoContent.text =
                        getString(R.styleable.HomeGameHelpLayout_home_game_help_content)
                } finally {
                    recycle()
                }
            }
        }
        addView(view)
    }
}