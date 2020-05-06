package com.yundom.utils.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import com.yundom.utils.BaseRobolectricTestCase
import com.yundom.utils.adapter.delegates.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class HeaderDelegateAdapterWrapperTest : BaseRobolectricTestCase() {
    @Mock
    lateinit var itemDelegateTypeA: ItemDelegateTypeA

    @Mock
    lateinit var itemDelegateTypeB: ItemDelegateTypeB

    @Mock
    lateinit var itemDelegateTypeC: ItemDelegateTypeC

    @Mock
    lateinit var itemDelegateTypeD: ItemDelegateTypeD

    @Mock
    lateinit var itemDelegateTypeE: ItemDelegateTypeE

    @Mock
    lateinit var itemDelegateTypeF: ItemDelegateTypeF

    @Mock
    lateinit var viewHolderTypeA: ViewHolderTypeA

    @Mock
    lateinit var viewHolderTypeB: ViewHolderTypeB

    @Mock
    lateinit var viewHolderTypeC: ViewHolderTypeC

    @Mock
    lateinit var viewHolderTypeD: ViewHolderTypeD

    @Mock
    lateinit var viewHolderTypeE: ViewHolderTypeE

    @Mock
    lateinit var viewHolderTypeF: ViewHolderTypeF

    @Mock
    lateinit var parentView: ViewGroup

    @Mock
    lateinit var itemView: View

    lateinit var wrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    lateinit var adapter: HeaderDelegateAdapterWrapper<TestingData2>

    @Before
    fun setUp() {
        whenever(itemDelegateTypeD.itemType()).thenReturn(TestingData2.TypeD::class.java)
        whenever(itemDelegateTypeE.itemType()).thenReturn(TestingData2.TypeE::class.java)
        whenever(itemDelegateTypeF.itemType()).thenReturn(TestingData2.TypeF::class.java)
        wrappedAdapter = spy(adapter())
        adapter = TestingHeaderAdapter(
                wrappedAdapter,
                itemDelegateTypeD,
                itemDelegateTypeE,
                itemDelegateTypeF
        )
    }

    @Test
    fun getHeaderCountAndGetItemCount() {
        adapter.items = createTestingData2Items(5)

        assertEquals(105, adapter.itemCount)
        assertEquals(5, adapter.headerCount)
    }

    @Test
    fun getItemViewType() {
        adapter.items = createTestingData2Items(5)

        (0 until 5).forEach {
            val viewType = adapter.getItemViewType(it)
            assertEquals(adapter.getItemViewTypeBase().or(it % 3), viewType)
        }

        (5 until 105).forEach {
            val viewType = adapter.getItemViewType(it)
            assertEquals((it - adapter.headerCount) % 3, viewType)
        }
    }

    @Test
    fun createViewHolder() {
        val order = Mockito.inOrder(
                itemDelegateTypeA,
                itemDelegateTypeB,
                itemDelegateTypeC,
                itemDelegateTypeD,
                itemDelegateTypeE,
                itemDelegateTypeF
        )

        (0 until 3).forEach {
            adapter.onCreateViewHolder(parentView, adapter.getItemViewTypeBase().or(it))
            when (it % 3) {
                0 -> order.verify(itemDelegateTypeD).createViewHolder(parentView)
                1 -> order.verify(itemDelegateTypeE).createViewHolder(parentView)
                else -> order.verify(itemDelegateTypeF).createViewHolder(parentView)
            }
        }

        (0 until 3).forEach {
            adapter.onCreateViewHolder(parentView, it)
            when (it % 3) {
                0 -> order.verify(itemDelegateTypeA).createViewHolder(parentView)
                1 -> order.verify(itemDelegateTypeB).createViewHolder(parentView)
                else -> order.verify(itemDelegateTypeC).createViewHolder(parentView)
            }
        }
    }

    @Test
    fun onBindViewHolder() {
        val order = Mockito.inOrder(
                itemDelegateTypeA,
                itemDelegateTypeB,
                itemDelegateTypeC,
                itemDelegateTypeD,
                itemDelegateTypeE,
                itemDelegateTypeF
        )
        val testingData = createTestingData2Items(5)
        val testingData2 = createTestingDataItems(100)
        adapter.items = testingData

        (0 until 5).forEach {
            when (it % 3) {
                0 -> adapter.onBindViewHolder(viewHolderTypeD, it)
                1 -> adapter.onBindViewHolder(viewHolderTypeE, it)
                else -> adapter.onBindViewHolder(viewHolderTypeF, it)
            }

            when (it % 3) {
                0 -> order.verify(itemDelegateTypeD).bindView(viewHolderTypeD, testingData[it] as TestingData2.TypeD)
                1 -> order.verify(itemDelegateTypeE).bindView(viewHolderTypeE, testingData[it] as TestingData2.TypeE)
                else -> order.verify(itemDelegateTypeF).bindView(viewHolderTypeF, testingData[it] as TestingData2.TypeF)
            }
        }

        (5 until 105).forEach {
            when ((it - adapter.headerCount) % 3) {
                0 -> adapter.onBindViewHolder(viewHolderTypeA, it)
                1 -> adapter.onBindViewHolder(viewHolderTypeB, it)
                else -> adapter.onBindViewHolder(viewHolderTypeC, it)
            }

            when ((it - adapter.headerCount) % 3) {
                0 -> order.verify(itemDelegateTypeA).bindView(viewHolderTypeA, testingData2[it - adapter.headerCount] as TestingData.TypeA)
                1 -> order.verify(itemDelegateTypeB).bindView(viewHolderTypeB, testingData2[it - adapter.headerCount] as TestingData.TypeB)
                else -> order.verify(itemDelegateTypeC).bindView(viewHolderTypeC, testingData2[it - adapter.headerCount] as TestingData.TypeC)
            }
        }
    }

    private fun adapter(): TestingAdapter {
        whenever(itemDelegateTypeA.itemType()).thenReturn(TestingData.TypeA::class.java)
        whenever(itemDelegateTypeB.itemType()).thenReturn(TestingData.TypeB::class.java)
        whenever(itemDelegateTypeC.itemType()).thenReturn(TestingData.TypeC::class.java)
        whenever(itemDelegateTypeA.createViewHolder(any())).thenReturn(ViewHolderTypeA(itemView))
        whenever(itemDelegateTypeB.createViewHolder(any())).thenReturn(ViewHolderTypeB(itemView))
        whenever(itemDelegateTypeC.createViewHolder(any())).thenReturn(ViewHolderTypeC(itemView))
        return TestingAdapter(itemDelegateTypeA, itemDelegateTypeB, itemDelegateTypeC).apply {
            items = createTestingDataItems(100)
        }
    }

    private fun createTestingData2Items(num: Int): List<TestingData2> {
        return (0 until num).map {
            when (it % 3) {
                0 -> TestingData2.TypeD("Type D")
                1 -> TestingData2.TypeE("Type E")
                else -> TestingData2.TypeF("Type F")
            }
        }
    }

    private fun createTestingDataItems(num: Int): List<TestingData> {
        return (0 until num).map {
            when (it % 3) {
                0 -> TestingData.TypeA("Type A")
                1 -> TestingData.TypeB("Type B")
                else -> TestingData.TypeC("Type C")
            }
        }
    }

    class TestingHeaderAdapter(
            adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
            itemD: ItemDelegateTypeD,
            itemE: ItemDelegateTypeE,
            itemF: ItemDelegateTypeF
    ) : HeaderDelegateAdapterWrapper<TestingData2>(
            adapter,
            itemD,
            itemE,
            itemF
    ) {
        override fun getItemViewTypeBase(): Int = 1 shl 15
    }

    open class TestingAdapter(
            itemA: ItemDelegateTypeA,
            itemB: ItemDelegateTypeB,
            itemC: ItemDelegateTypeC
    ) : DelegatesAdapter<TestingData>(
            itemA,
            itemB,
            itemC
    )
}
