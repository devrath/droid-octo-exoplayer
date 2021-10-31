package com.example.code.exoplayer.types.styled.util

import com.google.android.exoplayer2.Format

data class MplTrack(var name: String, var isSelected: Boolean, var groupIndex: Int, var trackIndex: Int,
                    var isAdaptive: Boolean, var format: Format?
)
