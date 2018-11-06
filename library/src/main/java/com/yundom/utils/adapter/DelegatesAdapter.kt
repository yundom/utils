package com.yundom.utils.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class DelegatesAdapter<I : Any>(
        vararg delegates: AdapterDelegate<RecyclerView.ViewHolder, I>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: List<I> = emptyList()

    private val itemTypeToIndexMap: Map<Class<*>, Int>
    private val delegates: List<AdapterDelegate<RecyclerView.ViewHolder, I>>

    init {
        itemTypeToIndexMap = delegates.mapIndexed { index, delegate ->
            delegate.itemType() to index
        }.toMap()
        this.delegates = delegates.map { it }
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
}
