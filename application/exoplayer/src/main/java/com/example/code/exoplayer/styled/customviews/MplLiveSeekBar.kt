package com.example.code.exoplayer.styled.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import kotlinx.coroutines.*
import timber.log.Timber

class MplLiveSeekBar : AppCompatSeekBar, Player.Listener {

    private val TAG = "MplLiveSeekBar"

    private var isControllerVisible = true
    private var player: Player? = null
    private var progressUpdateJob: Job? = null
    private val maxProgress = 10000
    private val progressUpdateDelay = 500L
    private var liveOffsetInMs = 45000L

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        max = maxProgress
    }

    fun setPlayer(player: SimpleExoPlayer) {
        this.player = player
        Timber.tag(TAG).d("Player Duration : ${player.duration}")
    }

    fun setLiveOffset(liveOffset: Long) {
        this.liveOffsetInMs = liveOffset
    }

    fun setControllerVisibility(isControllerVisible: Boolean) {
        Timber.tag(TAG).d("setControllerVisibility : $isControllerVisible")
        this.isControllerVisible = isControllerVisible
        if (isControllerVisible) {
            startUpdatingProgress()
        } else {
            stopUpdatingProgress()
        }
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
        updateProgress()
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        super.onTimelineChanged(timeline, reason)
        updateProgress()
    }

    override fun onPlaybackStateChanged(state: Int) {
        super.onPlaybackStateChanged(state)
        updateProgress()
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        updateProgress()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        updateProgress()
    }

    private fun updateProgress() {
        Timber.tag(TAG).d("updateProgress")
        player?.let {
            progress = if (it.contentDuration > liveOffsetInMs) {
                ((it.contentPosition.toFloat() / (it.contentDuration - liveOffsetInMs).toFloat()) * maxProgress).toInt()
            } else {
                maxProgress
            }

            secondaryProgress = if (it.contentDuration > liveOffsetInMs) {
                ((it.contentBufferedPosition.toFloat() / (it.contentDuration - liveOffsetInMs).toFloat()) * maxProgress).toInt()
            } else {
                maxProgress
            }
        }
    }

    fun onStopTrackingTouch(seekBar: SeekBar) {
        player?.takeIf { it.contentDuration > liveOffsetInMs }?.apply {
            seekTo((seekBar.progress * (contentDuration - liveOffsetInMs)) / maxProgress)
        }
        startUpdatingProgress()
    }

    private fun startUpdatingProgress() {
        Timber.tag(TAG).d("startUpdatingProgress")
        if (isAttachedToWindow) {
            stopUpdatingProgress()
            progressUpdateJob = CoroutineScope(Dispatchers.Main).launch {
                while (isAttachedToWindow && isControllerVisible) {
                    updateProgress()
                    delay(progressUpdateDelay)
                }
            }
            this.player?.addListener(this)
        }
    }

    fun onUIControllerStart() {
        startUpdatingProgress()
    }

    fun onUIControllerStop() {
        stopUpdatingProgress()
    }

    private fun stopUpdatingProgress() {
        Timber.tag(TAG).d("stopUpdatingProgress")
        progressUpdateJob?.cancel()
        progressUpdateJob = null
        this.player?.removeListener(this)
    }

    override fun onDetachedFromWindow() {
        stopUpdatingProgress()
        this.player?.removeListener(this)
        player = null
        super.onDetachedFromWindow()
    }
}