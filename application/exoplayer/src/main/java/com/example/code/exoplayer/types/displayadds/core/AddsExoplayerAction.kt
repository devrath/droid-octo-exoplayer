package com.example.code.exoplayer.types.displayadds.core

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class AddsExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : AddsExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : AddsExoplayerAction()
}