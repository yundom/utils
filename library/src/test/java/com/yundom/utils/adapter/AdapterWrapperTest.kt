package com.yundom.utils.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.yundom.utils.BaseRobolectricTestCase
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class AdapterWrapperTest : BaseRobolectricTestCase() {
    @Mock
    lateinit var recyclerView: RecyclerView

    @Mock
    lateinit var dataObserver: RecyclerView.AdapterDataObserver

    private lateinit var wrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    private lateinit var adapterWrapper: AdapterWrapper<RecyclerView.ViewHolder>

    @Before
    fun setUp() {
        wrappedAdapter = spy(adapter())
        wrappedAdapter.setHasStableIds(true)
        adapterWrapper = spy(AdapterWrapper(wrappedAdapter))
    }

    @Test
    fun testOnAttachedToRecyclerView() {
        adapterWrapper.onAttachedToRecyclerView(recyclerView)

        verify(wrappedAdapter, times(1)).onAttachedToRecyclerView(recyclerView)
    }

    @Test
    fun testOnChanged() {
        adapterWrapper.registerAdapterDataObserver(dataObserver)

        adapterWrapper.notifyDataSetChanged()

        verify(dataObserver, times(1)).onChanged()
    }

    @Test
    fun testWrappedAdapterOnChanged() {
        adapterWrapper.registerAdapterDataObserver(dataObserver)

        adapterWrapper.notifyDataSetChanged()

        verify(dataObserver, times(1)).onChanged()
        verify(adapterWrapper, times(1)).onWrappedAdapterChanged()
    }

    @Test
    fun testHasStableId() {
        assertTrue(adapterWrapper.hasStableIds())
    }

    private fun adapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return TestAdapter()
    }

    open class TestAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        @Mock var itemView: View = mock()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return TestViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        }
    }

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
