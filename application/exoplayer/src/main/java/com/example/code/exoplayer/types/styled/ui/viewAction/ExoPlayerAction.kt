package com.example.code.exoplayer.types.styled.ui.viewAction

import com.example.code.exoplayer.types.styled.util.LiveStatus
import com.example.code.exoplayer.types.styled.util.MplTrack
import com.example.code.exoplayer.types.styled.util.MplTrackList

sealed class ExoPlayerAction {

    class Validation(val isSuccess: Boolean, val url: String?) : ExoPlayerAction()
    class Progressbar(val isVisible: Boolean) : ExoPlayerAction()
    class ShowReplay(val show: Boolean) : ExoPlayerAction()
    class ShowPlayPause(val show: Boolean) : ExoPlayerAction()
    class SetForwardIconVisibility(val visible : Boolean) : ExoPlayerAction()
    object FirstFrameRendered : ExoPlayerAction()
    class EnableTracks(val enable: Boolean) : ExoPlayerAction()
    class TracksChange(val tracks: MplTrackList?) : ExoPlayerAction()
    class LiveView(val liveStatus: LiveStatus) : ExoPlayerAction()
    object DismissVideoQualityBottomSheet : ExoPlayerAction()
    class ShowSnackBar(val text : String) : ExoPlayerAction()
    class ShowError(val error : String, val currentPosition: Long?) : ExoPlayerAction()
    class SelectedTrack(val track: MplTrack?) : ExoPlayerAction()

    /*
     class ReminderError(val message: String?, val flag: Boolean) : ExoPlayerAction()
     class ReminderSuccess(val flag: Boolean) : ExoPlayerAction()
     */

}

