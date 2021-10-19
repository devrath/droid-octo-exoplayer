package com.example.code.exoplayer.styled.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import com.example.code.exoplayer.databinding.CustomStyledPlayerControlViewBinding
import com.example.code.exoplayer.databinding.CustomStyledPlayerViewBinding
import com.example.code.extensions.setVisible
import com.google.android.exoplayer2.SimpleExoPlayer
import timber.log.Timber

class CustomStyledPlayerView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attributes, defStyleAttr), SeekBar.OnSeekBarChangeListener {

    private val TAG = "CustomStyledPlayerView"

    private var player: SimpleExoPlayer? = null
    private val behindLiveOffset: Long = 45000L
    private val controllerTimeOutInMs = 5000

    val playerBinding by lazy(LazyThreadSafetyMode.NONE) {
        CustomStyledPlayerViewBinding.inflate(LayoutInflater.from(context))
    }

    val playerCtrlBinding by lazy(LazyThreadSafetyMode.NONE) {
       CustomStyledPlayerControlViewBinding.inflate(LayoutInflater.from(context))
    }

    fun setPlayer(simpleExoPlayer: SimpleExoPlayer) {
        this.player = simpleExoPlayer
        playerBinding.playerView.player = simpleExoPlayer
        playerCtrlBinding.mplLiveSeekbar.setPlayer(simpleExoPlayer)
        updateSeekbarVisibility()
    }

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


    interface Callback {
        fun showChat(show: Boolean)
        fun onPlayWhenReady(playWhenReady: Boolean)
        fun onFastForward(startTime: Long, targetTime: Long)
        fun onRewind(startTime: Long, targetTime: Long)
        fun onSeek(startTime: Long, targetTime: Long)
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}

    override fun onStartTrackingTouch(p0: SeekBar?) {
        playerBinding.playerView.controllerShowTimeoutMs = -1
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        playerBinding.playerView.controllerShowTimeoutMs = controllerTimeOutInMs
        playerCtrlBinding.mplLiveSeekbar.onStopTrackingTouch(seekBar)
    }
}