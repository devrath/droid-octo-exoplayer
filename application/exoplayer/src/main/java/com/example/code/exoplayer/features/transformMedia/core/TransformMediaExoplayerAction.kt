package com.example.code.exoplayer.features.transformMedia.core

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class TransformMediaExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : TransformMediaExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : TransformMediaExoplayerAction()
}