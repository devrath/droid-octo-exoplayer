package com.example.code.exoplayer.features.trackselection.core

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class TrackSelectionExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : TrackSelectionExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : TrackSelectionExoplayerAction()
}