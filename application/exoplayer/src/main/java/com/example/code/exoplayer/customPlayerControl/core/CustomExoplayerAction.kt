package com.example.code.exoplayer.customPlayerControl.core

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class CustomExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : CustomExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : CustomExoplayerAction()
}