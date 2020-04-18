package com.yundom.utils.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DelegatesAdapter<I : Any>(
        vararg delegates: ItemDelegate<out RecyclerView.ViewHolder, out I>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: List<I> = emptyList()

    private val itemTypeToIndexMap: Map<Class<*>, Int>
    private val delegates: List<ItemDelegate<RecyclerView.ViewHolder, I>>

    init {
        itemTypeToIndexMap = delegates.mapIndexed { index, delegate ->
            delegate.itemType() to index
        }.toMap()
        this.delegates = delegates.map {
            it as? ItemDelegate<RecyclerView.ViewHolder, I>
                    ?: throw IllegalArgumentException("Unsupported delegate type.")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegates[viewType].createViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        items[position].let {
            itemTypeToIndexMap[it.javaClass]
        }?.let {
            delegates[it]
        }?.bindView(holder, item)
    }

    override fun getItemViewType(position: Int): Int =
            itemTypeToIndexMap[items[position]::class.java]
                    ?: throw IllegalArgumentException("Unknown delegate type.")
}
