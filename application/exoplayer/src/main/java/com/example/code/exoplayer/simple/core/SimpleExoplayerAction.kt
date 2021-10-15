package com.example.code.exoplayer.simple.core

import com.example.code.exoplayer.customPlayerControl.core.CustomExoplayerAction
import com.google.android.exoplayer2.SimpleExoPlayer

sealed class SimpleExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : SimpleExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : SimpleExoplayerAction()
}