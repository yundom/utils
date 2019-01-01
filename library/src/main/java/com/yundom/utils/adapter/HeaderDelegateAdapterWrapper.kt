package com.yundom.utils.adapter

import android.content.pm.ApplicationInfo
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class HeaderDelegateAdapterWrapper<I : Any>(
        private val wrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        vararg delegates: ItemDelegate<out RecyclerView.ViewHolder, out I>
) : AdapterWrapper<RecyclerView.ViewHolder>(wrappedAdapter) {
    private val itemTypeToIndexMap: Map<Class<*>, Int>

    private val delegates: List<ItemDelegate<RecyclerView.ViewHolder, I>>

    var items: List<I> = emptyList()

    var headerCount: Int = items.size
        get() = items.size

    init {
        itemTypeToIndexMap = delegates.mapIndexed { index, delegate ->
            delegate.itemType() to getItemViewTypeBase().or(index)
        }.toMap()
        this.delegates = delegates.map {
            it as? ItemDelegate<RecyclerView.ViewHolder, I>
                    ?: throw IllegalArgumentException("Unsupported delegate type.")
        }
    }

    override fun getItemViewType(position: Int): Int =
            if (isHeaderPosition(position)) {
                itemTypeToIndexMap[items[position]::class.java]
                        ?: throw IllegalArgumentException("Unknown delegate type.")
            } else {
                wrappedAdapter.getItemViewType(positionWithoutHeader(position))
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (isHeaderViewType(viewType)) {
                delegates[trimHeaderViewTypeMask(viewType)].createViewHolder(parent)
            } else {
                wrappedAdapter.onCreateViewHolder(parent, viewType)
            }

    override fun getItemCount(): Int = wrappedAdapter.itemCount + headerCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderPosition(position)) {
            onBindHeaderViewHolder(holder, position)
        } else {
            wrappedAdapter.onBindViewHolder(holder, positionWithoutHeader(position))
        }
    }

    abstract fun getItemViewTypeBase(): Int

    private fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        items[position].let {
            itemTypeToIndexMap[it.javaClass]
        }?.let {
            delegates[trimHeaderViewTypeMask(it)]
        }?.bindView(holder, item)
    }

    private fun isHeaderPosition(position: Int) =
            headerCount > 0 && position < headerCount

    private fun isHeaderViewType(viewType: Int) =
            viewType.and(getItemViewTypeBase()) == getItemViewTypeBase()

    private fun trimHeaderViewTypeMask(viewType: Int) =
            viewType.and(getItemViewTypeBase().inv())

    private fun positionWithoutHeader(position: Int): Int =
            if (isHeaderPosition(position)) {
                position
            } else {
                position - headerCount
            }
}
