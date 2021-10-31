package com.example.code.exoplayer.types.styled.core

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class StyledExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : StyledExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : StyledExoplayerAction()
}