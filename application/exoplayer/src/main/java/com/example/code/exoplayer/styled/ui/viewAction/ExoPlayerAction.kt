package com.example.code.exoplayer.styled.ui.viewAction

import com.example.code.exoplayer.styled.util.LiveStatus
import com.example.code.exoplayer.styled.util.MplTrack

sealed class ExoPlayerAction {

    class Validation(val isSuccess: Boolean, val url: String?) : ExoPlayerAction()
    class Progressbar(val isVisible: Boolean) : ExoPlayerAction()
    class ShowReplay(val show: Boolean) : ExoPlayerAction()
    class ShowPlayPause(val show: Boolean) : ExoPlayerAction()
    class SetForwardIconVisibility(val visible : Boolean) : ExoPlayerAction()
   /* class ShowSnackBar(val text : String) : ExoPlayerAction()
    object DismissVideoQualityBottomSheet : ExoPlayerAction()
    object FirstFrameRendered : ExoPlayerAction()
    class ReminderError(val message: String?, val flag: Boolean) : ExoPlayerAction()
    class ReminderSuccess(val flag: Boolean) : ExoPlayerAction()
    class ShowError(val error : String, val currentPosition: Long?) : ExoPlayerAction()
    class LiveView(val liveStatus: LiveStatus) : ExoPlayerAction()
    class SelectedTrack(val track: MplTrack?) : ExoPlayerAction()
    class EnableTracks(val enable: Boolean) : ExoPlayerAction()*/

}

