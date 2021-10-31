package com.example.code.exoplayer.types.styled.util

data class MplVideo
constructor(
    val url: String,
    val sourceType: Int,
    val isVOD: Boolean,
    val startPosition: Long
) {

    companion object {
        const val SOURCE_TYPE_HLS = 1;
        const val SOURCE_TYPE_DASH = 2;
        const val SOURCE_TYPE_VOD = 3;
        const val SOURCE_TYPE_LOCAL = 4;
    }

}