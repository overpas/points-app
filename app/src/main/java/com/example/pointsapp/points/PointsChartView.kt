package com.example.pointsapp.points

import android.content.Context
import android.util.AttributeSet
import android.view.View

class PointsChartView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
) : View(
    context,
    attrs,
    defStyleAttr,
    defStyleRes,
) {

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : this(
        context,
        attrs,
        defStyleAttr,
        0,
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
    ) : this(
        context,
        attrs,
        0,
    )

    constructor(
        context: Context,
    ) : this(
        context,
        null,
    )
}
