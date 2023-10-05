package com.example.code.exoplayer.types.retrievingmetadata.core

import com.example.code.exoplayer.types.simple.core.SimpleExoplayerAction
import com.google.android.exoplayer2.SimpleExoPlayer

sealed class RetrievingMetadataExoplayerAction {
    data class  BindCustomExoplayer(val simpleExoplayer: SimpleExoPlayer) : RetrievingMetadataExoplayerAction()
    data class  ProgressBarVisibility(val isVisible: Boolean) : RetrievingMetadataExoplayerAction()
}