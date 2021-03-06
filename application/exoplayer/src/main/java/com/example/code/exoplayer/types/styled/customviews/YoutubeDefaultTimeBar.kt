package com.example.code.exoplayer.types.styled.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.code.exoplayer.R
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.Player


class YoutubeDefaultTimeBar : DefaultTimeBar {

    private val TAG = "YoutubeDefaultTimeBar"

    companion object {
        const val DIVIDER_WIDTH = 5F
        const val PROGRESS_LEFT_RIGHT_PADDING = 32F
    }

    private var player: Player? = null
    var durationSet : Boolean = false
    var realDurationMillis : Long = 0

    // List of points for creating sections
    var indicatorPositions: List<Float> = listOf(0.13F, 0.34F, 0.50F, 0.85F, 0.92F)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context,
        attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val paintIndicator = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.screen_background)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawProgressDivider(canvas)
    }

    private fun drawProgressDivider(canvas: Canvas) {
        indicatorPositions.forEach {
            val barPositionCenter = it * width()
            val barPositionLeft = barPositionCenter - DIVIDER_WIDTH
            val barPositionRight = barPositionCenter + DIVIDER_WIDTH

            drawOnCanvas(canvas, paintIndicator, barPositionLeft, barPositionRight)
        }
    }

    private fun width(): Float {
        return measuredWidth.toFloat()
    }

    private fun drawOnCanvas(canvas: Canvas, paint:Paint, progressLeft: Float, progressRight: Float) {
        //val barTop = (measuredHeight - barHeight) / 2
        //val barBottom = (measuredHeight + barHeight) / 2
        val progressTop = height / 1.8 - minimumHeight / 2
        val progressBottom = progressTop / 1.3 + minimumHeight / 2

        val barRect = RectF(progressLeft, progressTop.toFloat(),
            progressRight, progressBottom.toFloat())
        canvas.drawRoundRect(barRect, 50F, 50F, paint)
    }

}