package com.example.code.exoplayer.styled.util

import timber.log.Timber
import java.lang.Exception

class MplTrackList : ArrayList<MplTrack>() {

    companion object {
        const val TAG = "MplTrackList"
    }

    fun selectTrack(selectedTrack: MplTrack) {
        Timber.tag(TAG).d("selectTrack")
        try {
            forEach {
                it.isSelected = ((selectedTrack.groupIndex == it.groupIndex) && (selectedTrack.trackIndex == it.trackIndex))
            }
        } catch (e : Exception) {
            Timber.tag(TAG).d("Error in selectTrack")

        }
    }

}