package com.yundom.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecorator(
        private val verticalSpacing: Int,
        private val horizontalSpacing: Int,
        private val columnsCount: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        outRect.apply {
            left = horizontalSpacing
            top = if (position < columnsCount) verticalSpacing else 0
            right = if ((position + 1) % columnsCount == 0) horizontalSpacing else 0
            bottom = verticalSpacing
        }
    }
}
