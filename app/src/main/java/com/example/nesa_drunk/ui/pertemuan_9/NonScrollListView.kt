package com.example.nesa_drunk.ui.pertemuan_9

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

/**
 * A custom ListView that calculates its height based on its children.
 * Useful when placed inside a NestedScrollView.
 */
class NonScrollListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ListView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMeasureSpecCustom = View.MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpecCustom)
        layoutParams.height = measuredHeight
    }
}
