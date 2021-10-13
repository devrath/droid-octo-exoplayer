package com.example.code.exoplayer

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class ExoplayerAction {
    data class  BindExoplayer(val simpleExoplayer: SimpleExoPlayer) : ExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : ExoplayerAction()
}