package com.example.code.exoplayer.styled.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import com.example.code.exoplayer.R

enum class SeekbarElementType { PROGRESS_BAR, PROGRESS_INDICATOR, PROGRESS_DIVIDER }

class YoutubeSegmentedSeekBar : AppCompatSeekBar {

    companion object {
        const val DIVIDER_WIDTH = 5F
        const val PROGRESS_LEFT_RIGHT_PADDING = 32F
    }

    // List of points for creating sections
    lateinit var indicatorPositions: List<Float>

    /** ************************ Paint Objects ************************ **/
    private val paintProgressBar = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.color_progress_bkg_tint)
    }
    private val paintProgress = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.color_thumb_tint)
    }
    private val paintIndicator = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.screen_background)
    }
    /** ************************ Paint Objects ************************ **/

    /** ************************ Constructors ************************ **/
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int)
            : super(context!!, attrs, defStyle)
    /** ************************ Constructors ************************ **/

    private var progress = 0F
        set(state) {
            field = state
            invalidate()
        }

    // Use to get the width of the view
    private fun width(): Float {
        return measuredWidth.toFloat()
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        performDraw(canvas, SeekbarElementType.PROGRESS_BAR)
        //performDraw(canvas,SeekbarElementType.PROGRESS_INDICATOR)
        performDraw(canvas, SeekbarElementType.PROGRESS_DIVIDER)
    }

    /** ************************ Draw progress bar elements ************************ **/
    // Draw the progress bar
    private fun drawProgressBar(canvas: Canvas) {
        //drawCenteredBar(canvas, 0F, width())
        drawOnCanvas(
            canvas, paintProgressBar, PROGRESS_LEFT_RIGHT_PADDING,
            width()- PROGRESS_LEFT_RIGHT_PADDING
        )
    }

    // Draw the progress
    private fun drawProgress(canvas: Canvas) {
        val barWidth = (progress) * width()
        drawOnCanvas(canvas, paintProgress,0F, barWidth)
    }

    // Draw the indicator
    private fun drawProgressDivider(canvas: Canvas) {
        indicatorPositions.forEach {
            val barPositionCenter = it * width()
            val barPositionLeft = barPositionCenter - DIVIDER_WIDTH
            val barPositionRight = barPositionCenter + DIVIDER_WIDTH

            drawOnCanvas(canvas, paintIndicator, barPositionLeft, barPositionRight)
        }
    }
    /** ************************ Draw progress bar elements ************************ **/


    private fun performDraw(canvas: Canvas, type: SeekbarElementType) {
        when(type){
            SeekbarElementType.PROGRESS_BAR -> drawProgressBar(canvas)
            SeekbarElementType.PROGRESS_INDICATOR -> drawProgress(canvas)
            SeekbarElementType.PROGRESS_DIVIDER -> drawProgressDivider(canvas)
        }
    }

    /**
     * This function is used to draw on the canvas
     * * This function is a common function for
     * * 1 * Drawing the progress bar
     * * 2 * Drawing the Indicator
     * * 3 * Drawing the progress for the progress bar
     */
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