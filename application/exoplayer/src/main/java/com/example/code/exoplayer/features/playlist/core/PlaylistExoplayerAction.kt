package com.example.code.exoplayer.features.playlist.core

import com.google.android.exoplayer2.SimpleExoPlayer

sealed class PlaylistExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : PlaylistExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : PlaylistExoplayerAction()
}