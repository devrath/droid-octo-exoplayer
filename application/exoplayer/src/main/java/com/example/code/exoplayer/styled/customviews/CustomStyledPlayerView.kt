package com.example.code.exoplayer.styled.customviews

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.CustomStyledPlayerViewBinding
import com.example.code.exoplayer.styled.core.MplControlDispatcher
import com.example.code.extensions.setVisible
import com.example.code.extensions.setVisibleOrInvisible
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.material.textview.MaterialTextView
import timber.log.Timber
import android.view.MotionEvent
import android.widget.ImageButton
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SeekParameters.CLOSEST_SYNC


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


    private val playerBinding = CustomStyledPlayerViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        playerBinding.playerView.apply {
            // Set the control dispatcher
            setControlDispatcher(mplControlDispatcher)
            // set the visibility listener for controller
            setControllerVisibilityListener {
                findViewById<MplLiveSeekBar>(R.id.mpl_live_seekbar).setControllerVisibility(it == View.VISIBLE)
            }
        }
    }

    fun setStartAndStopSeekBar(isStart : Boolean = true) {
        if(isStart){
            //findViewById<MplLiveSeekBar>(R.id.mpl_live_seekbar).onUIControllerStart()
        }else{
            //findViewById<MplLiveSeekBar>(R.id.mpl_live_seekbar).onUIControllerStop()
        }
    }


    fun setCallback(callback: Callback) {
        this.callback = callback
        mplControlDispatcher.setCallback(callback)
    }

    fun setPlayer(simpleExoPlayer: SimpleExoPlayer) {
        this.player = simpleExoPlayer
        playerBinding.playerView.player = simpleExoPlayer
        updateSeekbarVisibility()
    }

    /*** *************************** Set click listeners *************************** ***/
    fun showPlayPauseIcon(show: Boolean) {
        findViewById<AppCompatImageButton>(R.id.exo_play_pause).setVisibleOrInvisible(show)
    }

    fun showReplayIcon(show: Boolean) {
        playerBinding.replay.setVisibleOrInvisible(show)
    }

    fun setTracksEnabled(enable: Boolean) {
        findViewById<AppCompatImageButton>(R.id.changeQuality).isEnabled  = enable
    }

    fun setOnQualityChangeClickListener(listener: OnClickListener) {
        findViewById<AppCompatImageView>(R.id.changeQuality).setOnClickListener(listener)
    }

    fun setOnLiveClickListener(listener: OnClickListener) {
        playerBinding.liveText.setOnClickListener(listener)
    }

    fun setOnGoLiveClickListener(listener: OnClickListener) {
        findViewById<MaterialTextView>(R.id.goLiveText).setOnClickListener(listener)
    }

    fun setOnCloseClickListener(listener: OnClickListener) {
        findViewById<AppCompatImageView>(R.id.closeIcon).setOnClickListener(listener)
    }

    fun setOnFullScreenClickListener(listener: OnClickListener) {
        findViewById<AppCompatImageView>(R.id.fullScreen).setOnClickListener(listener)
    }

    fun setOnScreenRotateClickListener(listener: OnClickListener) {
        findViewById<AppCompatImageView>(R.id.rotateScreen).setOnClickListener(listener)
    }

    fun setOnReplayClickListener(listener: OnClickListener) {
        playerBinding.replay.setOnClickListener(listener)
    }

    private fun setFullscreenIconsVisibility(showRotateIcon: Boolean, showFullScreenIcon: Boolean) {
        findViewById<AppCompatImageView>(R.id.fullScreen).setVisible(showFullScreenIcon)
        findViewById<AppCompatImageView>(R.id.rotateScreen).setVisible(showRotateIcon)
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
        findViewById<AppCompatImageView>(R.id.shareIcon).setOnClickListener(listener)
    }
    /*** *************************** Set click listeners *************************** ***/

    /*** ******************* SEEK-BAR Over-Ridden Methods ******************* ***/
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}

    override fun onStartTrackingTouch(p0: SeekBar?) {
        playerBinding.playerView.controllerShowTimeoutMs = -1
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        playerBinding.playerView.controllerShowTimeoutMs = controllerTimeOutInMs
        findViewById<MplLiveSeekBar>(R.id.mpl_live_seekbar).onStopTrackingTouch(seekBar)
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
            findViewById<DefaultTimeBar>(R.id.exo_progress).setVisible(true)
        }
    }

    fun setPosition() {

        val defaultTimeBar = findViewById<DefaultTimeBar>(R.id.exo_progress)

        //if(defaultTimeBar.isd)

       /* player?.let{
            val maxValue = it.maxSeekToPreviousPosition
            val currentValue = it.currentPosition

            it.seekTo(it.currentPosition + 5000)
        }

        player?.addListener(ExoPlayer)*/


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