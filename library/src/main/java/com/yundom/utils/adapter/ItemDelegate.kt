package com.yundom.utils.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ItemDelegate<H : RecyclerView.ViewHolder, I> {
    fun itemType(): Class<out I>

    fun createViewHolder(parent: ViewGroup): H

    fun bindView(viewHolder: H, item: I)
}
