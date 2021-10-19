package com.example.code.exoplayer.styled.core

import com.example.code.exoplayer.styled.customviews.CustomStyledPlayerView
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player
import timber.log.Timber

class MplControlDispatcher(private val fastForwardTimeInMs: Long,
                           private val rewindTimeInMs: Long)
    : DefaultControlDispatcher(fastForwardTimeInMs, rewindTimeInMs) {

    companion object {
        const val TAG = "MplControlDispatcher"
    }

    private var callback: CustomStyledPlayerView.Callback? = null

    fun setCallback(callback: CustomStyledPlayerView.Callback) {
        this.callback = callback
    }

    override fun dispatchSeekTo(player: Player, windowIndex: Int, positionMs: Long): Boolean {
        Timber.tag(TAG).d( "dispatchSeekTo -> [CurrentPosition : ${player.currentPosition}, PositionMs : $positionMs]")
        callback?.onSeek(player.currentPosition, positionMs)
        return super.dispatchSeekTo(player, windowIndex, positionMs)
    }

    override fun dispatchSetPlayWhenReady(player: Player, playWhenReady: Boolean): Boolean {
        callback?.onPlayWhenReady(playWhenReady)
        return super.dispatchSetPlayWhenReady(player, playWhenReady)
    }

    override fun dispatchFastForward(player: Player): Boolean {
        callback?.onFastForward(player.currentPosition,
            if (player.currentPosition + fastForwardTimeInMs > player.duration) { player.duration }
            else { player.currentPosition + fastForwardTimeInMs }
        )
        return super.dispatchFastForward(player)
    }

    override fun dispatchRewind(player: Player): Boolean {
        callback?.onRewind(player.currentPosition,
            if (player.currentPosition - rewindTimeInMs < 0) { 0 }
            else { player.currentPosition - rewindTimeInMs }
        )
        return super.dispatchRewind(player)
    }

}