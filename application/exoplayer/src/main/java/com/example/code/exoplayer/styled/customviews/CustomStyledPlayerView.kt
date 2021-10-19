package com.example.code.exoplayer.styled.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.code.exoplayer.databinding.CustomStyledPlayerControlViewBinding
import com.example.code.exoplayer.databinding.CustomStyledPlayerViewBinding
import com.example.code.exoplayer.styled.core.MplControlDispatcher
import com.example.code.extensions.setVisible
import com.example.code.extensions.setVisibleOrInvisible
import com.google.android.exoplayer2.SimpleExoPlayer
import timber.log.Timber

class CustomStyledPlayerView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attributes, defStyleAttr), SeekBar.OnSeekBarChangeListener {

    companion object {
        const val TAG = "ExoPlayer : MplPlayerView"
        const val fastForwardTimeInMs = 10000L
        const val rewindTimeInMs = 10000L
        const val controllerTimeOutInMs = 5000
        const val behindLiveOffset: Long = 45000L
    }

    private val TAG = "CustomStyledPlayerView"

    private var player: SimpleExoPlayer? = null
    private var callback: Callback? = null
    private var mplControlDispatcher: MplControlDispatcher =
        MplControlDispatcher(fastForwardTimeInMs, rewindTimeInMs)


    val playerBinding by lazy(LazyThreadSafetyMode.NONE) {
        CustomStyledPlayerViewBinding.inflate(LayoutInflater.from(context))
    }

    val playerCtrlBinding by lazy(LazyThreadSafetyMode.NONE) {
       CustomStyledPlayerControlViewBinding.inflate(LayoutInflater.from(context))
    }


    init {
        playerBinding.playerView.apply {
            // Set the control dispatcher
            setControlDispatcher(mplControlDispatcher)
            // set the visibility listener for controller
            setControllerVisibilityListener {
                playerCtrlBinding.mplLiveSeekbar.setControllerVisibility(it == View.VISIBLE)
            }
        }
    }


    fun setCallback(callback: Callback) {
        this.callback = callback
        mplControlDispatcher.setCallback(callback)
    }

    fun setPlayer(simpleExoPlayer: SimpleExoPlayer) {
        this.player = simpleExoPlayer
        playerBinding.playerView.player = simpleExoPlayer
        playerCtrlBinding.mplLiveSeekbar.setPlayer(simpleExoPlayer)
        updateSeekbarVisibility()
    }

    /*** *************************** Set click listeners *************************** ***/
    fun showPlayPauseIcon(show: Boolean) {
        playerCtrlBinding.exoPlayPause.setVisibleOrInvisible(show)
    }

    fun showReplayIcon(show: Boolean) {
        playerBinding.replay.setVisibleOrInvisible(show)
    }

    fun setTracksEnabled(enable: Boolean) {
        playerCtrlBinding.changeQuality.isEnabled = enable
    }

    fun setOnQualityChangeClickListener(listener: OnClickListener) {
        playerCtrlBinding.changeQuality.setOnClickListener(listener)
    }

    fun setOnLiveClickListener(listener: OnClickListener) {
        playerBinding.liveText.setOnClickListener(listener)
    }

    fun setOnGoLiveClickListener(listener: OnClickListener) {
        playerCtrlBinding.goLiveText.setOnClickListener(listener)
    }

    fun setOnCloseClickListener(listener: OnClickListener) {
        playerCtrlBinding.closeIcon.setOnClickListener(listener)
    }

    fun setOnFullScreenClickListener(listener: OnClickListener) {
        playerCtrlBinding.fullScreen.setOnClickListener(listener)
    }

    fun setOnScreenRotateClickListener(listener: OnClickListener) {
        playerCtrlBinding.rotateScreen.setOnClickListener(listener)
    }

    fun setOnReplayClickListener(listener: OnClickListener) {
        playerBinding.replay.setOnClickListener(listener)
    }

    private fun setFullscreenIconsVisibility(showRotateIcon: Boolean, showFullScreenIcon: Boolean) {
        playerCtrlBinding.rotateScreen.setVisible(showRotateIcon)
        playerCtrlBinding.fullScreen.setVisible(showFullScreenIcon)
    }

    fun showHeartsCount(show: Boolean) {
        playerBinding.heartCount.setVisible(show)
    }

    fun showViewersCount(show: Boolean) {
        playerBinding.viewerCount.setVisible(show)
    }

    fun setHeartsCount(heartsCount: String) {
        playerBinding.heartCount.text = heartsCount
    }

    fun setViewersCount(viewersCount: String) {
        playerBinding.viewerCount.text = viewersCount
    }

    fun setOnShareClickListener(listener: OnClickListener) {
        playerCtrlBinding.shareIcon.setOnClickListener(listener)
    }
    /*** *************************** Set click listeners *************************** ***/

    /*** ******************* SEEK-BAR Over-Ridden Methods ******************* ***/
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}

    override fun onStartTrackingTouch(p0: SeekBar?) {
        playerBinding.playerView.controllerShowTimeoutMs = -1
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        playerBinding.playerView.controllerShowTimeoutMs = controllerTimeOutInMs
        playerCtrlBinding.mplLiveSeekbar.onStopTrackingTouch(seekBar)
    }
    /*** ******************* SEEK-BAR Over-Ridden Methods ******************* ***/

    private fun updateSeekbarVisibility() {
        Timber.tag(TAG).d("setSeekbarVisibility")
        player?.let { player ->
            updateSeekbarVisibility(player)
        }
    }

    private fun updateSeekbarVisibility(player: SimpleExoPlayer) {
        Timber.tag(TAG).d("isCurrentWindowLive -> ${player.isCurrentWindowLive}")
        playerBinding.playerView.apply {
            playerCtrlBinding.mplLiveSeekbar.setVisible(true)
            playerCtrlBinding.mplLiveSeekbar.setOnSeekBarChangeListener(this@CustomStyledPlayerView)
            playerCtrlBinding.exoProgress.setVisible(false)
            playerCtrlBinding.mplLiveSeekbar.setLiveOffset(behindLiveOffset)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playerBinding.playerView.player = null
    }

    interface Callback {
        //fun showChat(show: Boolean)
        fun onPlayWhenReady(playWhenReady: Boolean)
        fun onFastForward(startTime: Long, targetTime: Long)
        fun onRewind(startTime: Long, targetTime: Long)
        fun onSeek(startTime: Long, targetTime: Long)
    }

}