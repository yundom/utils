package com.yundom.utils.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface AdapterDelegate<H : RecyclerView.ViewHolder, I> {
    fun itemType(): Class<out I>

    fun createViewHolder(parent: ViewGroup): H

    fun bindView(viewHolder: H, item: I)
}
