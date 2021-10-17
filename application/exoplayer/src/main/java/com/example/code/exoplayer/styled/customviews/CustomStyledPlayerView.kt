package com.example.code.exoplayer.styled.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.code.exoplayer.databinding.CustomStyledPlayerViewBinding
import com.example.code.exoplayer.databinding.FragmentStyledExoPlayerBinding
import com.google.android.exoplayer2.SimpleExoPlayer

class CustomStyledPlayerView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attributes, defStyleAttr) {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        CustomStyledPlayerViewBinding.inflate(LayoutInflater.from(context))
    }


    fun setPlayer(simpleExoPlayer: SimpleExoPlayer) {
        binding.playerView.player = simpleExoPlayer
        //binding.playerView.mpl_live_seekbar.setPlayer(simpleExoPlayer)
        //updateSeekbarVisibility()
    }


    interface Callback {
        fun showChat(show: Boolean)
        fun onPlayWhenReady(playWhenReady: Boolean)
        fun onFastForward(startTime: Long, targetTime: Long)
        fun onRewind(startTime: Long, targetTime: Long)
        fun onSeek(startTime: Long, targetTime: Long)
    }
}