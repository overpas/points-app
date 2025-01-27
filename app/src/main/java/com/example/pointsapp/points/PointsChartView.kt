package com.example.pointsapp.points

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.pointsapp.R
import com.example.pointsapp.domain.model.Point
import kotlin.math.abs

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

    private var points: List<Point>? = null

    private var minX: Double = Double.MIN_VALUE
    private var minY: Double = Double.MIN_VALUE
    private var maxX: Double = Double.MAX_VALUE
    private var maxY: Double = Double.MAX_VALUE
    private var xSpan = 0.0
    private var ySpan = 0.0
    private var xOffset = 0.0
    private var yOffset = 0.0

    private val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val pointPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    private var pointRadius: Float = 2f

    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

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

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PointsChartView,
            defStyleAttr,
            defStyleRes,
        ).apply {
            try {
                axisPaint.strokeWidth = getDimensionPixelSize(
                    R.styleable.PointsChartView_axisWidth,
                    2,
                ).toFloat()
                axisPaint.color = getColor(R.styleable.PointsChartView_axisColor, Color.BLACK)
                pointRadius = getDimensionPixelSize(
                    R.styleable.PointsChartView_pointRadius,
                    2,
                ).toFloat()
                pointPaint.color = getColor(R.styleable.PointsChartView_pointColor, Color.BLACK)
                linePaint.strokeWidth = getDimensionPixelSize(
                    R.styleable.PointsChartView_lineWidth,
                    2,
                ).toFloat()
                linePaint.color = getColor(R.styleable.PointsChartView_lineColor, Color.BLACK)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // TODO Handle 0
        val xScale = width / xSpan.toFloat()
        val yScale = height / ySpan.toFloat()
        // Draw axes
        val xAxisX = xOffset.toFloat() * xScale
        val yAxisY = yOffset.toFloat() * yScale
        canvas.drawLine(xAxisX, 0f, xAxisX, height.toFloat(), axisPaint)
        canvas.drawLine(0f, yAxisY, width.toFloat(), yAxisY, axisPaint)
        // Draw points with lines
        var lastX: Float? = null
        var lastY: Float? = null
        points?.forEach { point ->
            val x = (point.x + xOffset).toFloat() * xScale
            val y = (point.y + yOffset).toFloat() * yScale
            if (lastX != null && lastY != null) {
                canvas.drawLine(lastX, lastY, x, y, linePaint)
            }
            canvas.drawCircle(x, y, pointRadius, pointPaint)
            lastX = x
            lastY = y
        }
    }

    fun setPoints(points: List<Point>) {
        this.points = points
        minX = points.minBy(Point::x).x
        minY = points.minBy(Point::y).y
        maxX = points.maxBy(Point::x).x
        maxY = points.maxBy(Point::y).y
        xSpan = maxX - minX
        ySpan = maxY - minY
        xOffset = if (minX < 0) abs(minX) else 0.0
        yOffset = if (minY < 0) abs(minY) else 0.0
        invalidate()
    }
}
