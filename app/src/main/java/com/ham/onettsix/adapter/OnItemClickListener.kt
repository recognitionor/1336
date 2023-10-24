package com.ham.onettsix.adapter

import android.view.View

interface OnItemClickListener<T> {

    fun onItemClick(item: T, index: Int, view: View? = null)

}