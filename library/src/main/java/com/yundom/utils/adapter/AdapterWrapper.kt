package com.yundom.utils.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

open class AdapterWrapper<VH : RecyclerView.ViewHolder>(
        private var wrappedAdapter: RecyclerView.Adapter<VH>) : RecyclerView.Adapter<VH>() {
    private var bridgeObserver: BridgeObserver<VH>?

    init {
        setHasStableIds(wrappedAdapter.hasStableIds())
        bridgeObserver = BridgeObserver(this).also {
            wrappedAdapter.registerAdapterDataObserver(it)
        }
        super.setHasStableIds(wrappedAdapter.hasStableIds())
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        wrappedAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        wrappedAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        wrappedAdapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        wrappedAdapter.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: VH) {
        wrappedAdapter.onViewRecycled(holder)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
        wrappedAdapter.setHasStableIds(hasStableIds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
            wrappedAdapter.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: VH, position: Int) {
        wrappedAdapter.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int = wrappedAdapter.itemCount

    override fun getItemViewType(position: Int): Int = wrappedAdapter.getItemViewType(position)

    override fun getItemId(position: Int): Long = wrappedAdapter.getItemId(position)

    protected fun onHandleWrappedAdapterChanged() {
        notifyDataSetChanged()
    }

    protected fun onHandleWrappedAdapterItemRangeChanged(positionStart: Int, itemCount: Int) {
        notifyItemRangeChanged(positionStart, itemCount)
    }

    protected fun onHandleWrappedAdapterItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        notifyItemRangeChanged(positionStart, itemCount, payload)
    }

    protected fun onHandleWrappedAdapterItemRangeInserted(positionStart: Int, itemCount: Int) {
        notifyItemRangeInserted(positionStart, itemCount)
    }

    protected fun onHandleWrappedAdapterItemRangeRemoved(positionStart: Int, itemCount: Int) {
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    protected fun onHandleWrappedAdapterRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        if (itemCount != 1) {
            throw IllegalStateException("itemCount should be always 1  (actual: $itemCount)")
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    internal fun onWrappedAdapterChanged() {
        onHandleWrappedAdapterChanged()
    }

    internal fun onWrappedAdapterItemRangeChanged(positionStart: Int, itemCount: Int) {
        onHandleWrappedAdapterItemRangeChanged(positionStart, itemCount)
    }

    internal fun onWrappedAdapterItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        onHandleWrappedAdapterItemRangeChanged(positionStart, itemCount, payload)
    }

    internal fun onWrappedAdapterItemRangeInserted(positionStart: Int, itemCount: Int) {
        onHandleWrappedAdapterItemRangeInserted(positionStart, itemCount)
    }

    internal fun onWrappedAdapterItemRangeRemoved(positionStart: Int, itemCount: Int) {
        onHandleWrappedAdapterItemRangeRemoved(positionStart, itemCount)
    }

    internal fun onWrappedAdapterRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        onHandleWrappedAdapterRangeMoved(fromPosition, toPosition, itemCount)
    }

    private class BridgeObserver<VH : RecyclerView.ViewHolder>(
            adapterWrapper: AdapterWrapper<VH>
    ) : RecyclerView.AdapterDataObserver() {
        private val adapterWrapperRef: WeakReference<AdapterWrapper<VH>> =
                WeakReference(adapterWrapper)

        override fun onChanged() {
            adapterWrapperRef.get()?.onWrappedAdapterChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            adapterWrapperRef.get()?.onWrappedAdapterItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            adapterWrapperRef.get()?.onWrappedAdapterItemRangeChanged(
                    positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            adapterWrapperRef.get()?.onWrappedAdapterItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            adapterWrapperRef.get()?.onWrappedAdapterItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            adapterWrapperRef.get()?.onWrappedAdapterRangeMoved(fromPosition, toPosition, itemCount)
        }
    }
}
