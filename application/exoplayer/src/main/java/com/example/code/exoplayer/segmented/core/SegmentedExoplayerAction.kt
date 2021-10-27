package com.example.code.exoplayer.segmented.core

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class SegmentedExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : SegmentedExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : SegmentedExoplayerAction()
}