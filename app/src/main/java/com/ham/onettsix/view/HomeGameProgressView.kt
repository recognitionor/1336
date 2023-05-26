package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ham.onettsix.R
import com.ham.onettsix.databinding.HomeGameProgressViewBinding

class HomeGameProgressView(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {

    private lateinit var binding: HomeGameProgressViewBinding

    init {
        Log.d("jhlee", "HomeGameProgressView")
        val view = inflate(context, R.layout.home_game_progress_view, null)
        binding = HomeGameProgressViewBinding.inflate(LayoutInflater.from(context))
        view.apply {}
        addView(binding.root)
    }
}