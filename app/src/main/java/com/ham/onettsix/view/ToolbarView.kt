package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.ham.onettsix.R
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.view.get
import com.ham.onettsix.databinding.ToolbarLayoutBinding
import java.lang.Exception

class ToolbarView(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {

    private var binding: ToolbarLayoutBinding

    private lateinit var menuLayout: LinearLayoutCompat

    init { // inflate binding and add as view
    }

    fun addMenu(view: View) {
        menuLayout.addView(view, 1)
    }

    fun removeMenu() {
        try {
            menuLayout.removeViewAt(1)
        } catch (ignored: Exception) {
        }
    }

    fun getBinding(): ToolbarLayoutBinding {
        return binding
    }

    fun setTitle(title: String) {
        binding.toolbarBackTitleTextview.text = title
    }

    init {
        val view = inflate(context, R.layout.toolbar_layout, null)
        binding = ToolbarLayoutBinding.inflate(LayoutInflater.from(context))
        menuLayout = binding.menuLayout
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