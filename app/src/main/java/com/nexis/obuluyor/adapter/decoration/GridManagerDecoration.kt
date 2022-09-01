package com.nexis.obuluyor.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridManagerDecoration(val vSize: Int, val hSize: Int, val aSize: Int) : RecyclerView.ItemDecoration() {
    private var aPos: Int = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        aPos = parent.getChildAdapterPosition(view)

        if (aPos < aSize)
            outRect.set(hSize, 0, hSize, vSize)
    }
}