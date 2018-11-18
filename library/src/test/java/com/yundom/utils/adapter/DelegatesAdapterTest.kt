package com.yundom.utils.adapter

import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.whenever
import com.yundom.utils.adapter.delegates.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DelegatesAdapterTest {
    @Spy
    lateinit var itemDelegateTypeA: ItemDelegateTypeA

    @Mock
    lateinit var itemDelegateTypeB: ItemDelegateTypeB

    @Mock
    lateinit var itemDelegateTypeC: ItemDelegateTypeC

    @Mock
    lateinit var parentView: ViewGroup

    @Mock
    lateinit var itemView: View

    private lateinit var adapter: TestingAdapter

    private lateinit var items: List<TestingData>

    @Before
    fun setUp() {
        whenever(itemDelegateTypeA.itemType()).thenReturn(TestingData.TypeA::class.java)
        whenever(itemDelegateTypeB.itemType()).thenReturn(TestingData.TypeB::class.java)
        whenever(itemDelegateTypeC.itemType()).thenReturn(TestingData.TypeC::class.java)
        adapter = TestingAdapter(itemDelegateTypeA, itemDelegateTypeB, itemDelegateTypeC)
        items = createItems(10)
        adapter.items = items
    }

    @Test
    fun onCreateViewHolder() {
        val order = Mockito.inOrder(itemDelegateTypeA, itemDelegateTypeB, itemDelegateTypeC)
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
    fun getItemCount() {
        assertEquals(10, adapter.itemCount)
    }

    @Test
    fun onBindViewHolder() {
        val viewHolderTypeA = ViewHolderTypeA(itemView)
        val viewHolderTypeB = ViewHolderTypeB(itemView)
        val viewHolderTypeC = ViewHolderTypeC(itemView)

        (0 until 10).forEach {
            val order = Mockito.inOrder(itemDelegateTypeA, itemDelegateTypeB, itemDelegateTypeC)
            when (it % 3) {
                0 -> {
                    adapter.onBindViewHolder(viewHolderTypeA, it)
                    order.verify(itemDelegateTypeA).bindView(
                            viewHolderTypeA,
                            items[it] as TestingData.TypeA
                    )
                }
                1 -> {
                    adapter.onBindViewHolder(viewHolderTypeB, it)
                    order.verify(itemDelegateTypeB).bindView(
                            viewHolderTypeB,
                            items[it] as TestingData.TypeB
                    )
                }
                else -> {
                    adapter.onBindViewHolder(viewHolderTypeC, it)
                    order.verify(itemDelegateTypeC).bindView(
                            viewHolderTypeC,
                            items[it] as TestingData.TypeC
                    )
                }
            }

        }
    }

    private fun createItems(num: Int): List<TestingData> {
        return (0 until num).map {
            when (it % 3) {
                0 -> TestingData.TypeA("Type A")
                1 -> TestingData.TypeB("Type B")
                else -> TestingData.TypeC("Type C")
            }
        }
    }

    class TestingAdapter(
            itemA: ItemDelegateTypeA,
            itemB: ItemDelegateTypeB,
            itemC: ItemDelegateTypeC
    ) : DelegatesAdapter<TestingData>(
            itemA,
            itemB,
            itemC
    )
}
