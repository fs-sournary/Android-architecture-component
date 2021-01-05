package com.sournary.architecturecomponent.widget

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView

/**
 * The decoration defines view that separate items in the movie Recycler's adapter.
 */
class MovieItemDecoration(
    private val horizontalSpacing: Int, private val divider: Drawable
) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val right = parent.width - horizontalSpacing
        parent.forEachIndexed { index, itemView ->
            if (index == parent.childCount - 1) return@forEachIndexed
            val top = itemView.bottom
            divider.setBounds(horizontalSpacing, top, right, top + divider.intrinsicHeight)
            divider.draw(c)
        }
    }
}
