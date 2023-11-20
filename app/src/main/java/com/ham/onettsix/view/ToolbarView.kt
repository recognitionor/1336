package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.ham.onettsix.R
import androidx.appcompat.widget.Toolbar
import com.ham.onettsix.databinding.ToolbarLayoutBinding

class ToolbarView(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {

    private var binding: ToolbarLayoutBinding

    init { // inflate binding and add as view


    }

    fun getBinding(): ToolbarLayoutBinding {
        return binding
    }

    init {
        val view = inflate(context, R.layout.toolbar_layout, null)
        binding = ToolbarLayoutBinding.inflate(LayoutInflater.from(context))
        view.apply {
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.ToolbarView, 0, 0
            ).apply {
                try {
                    binding.toolbarBackTitleTextview.text =
                        getString(R.styleable.ToolbarView_toolbar_title)
                    if (getBoolean(R.styleable.ToolbarView_toolbar_use_add_btn, false)) {
                        binding.addBtn.visibility = View.VISIBLE
                    } else {
                        binding.addBtn.visibility = View.GONE
                    }
                    if (getBoolean(R.styleable.ToolbarView_toolbar_back_btn, true)) {
                        binding.backBtn.visibility = View.VISIBLE
                    } else {
                        binding.backBtn.visibility = View.GONE
                    }
                } finally {
                    recycle()
                }
            }
        }
        addView(binding.root)
    }
}