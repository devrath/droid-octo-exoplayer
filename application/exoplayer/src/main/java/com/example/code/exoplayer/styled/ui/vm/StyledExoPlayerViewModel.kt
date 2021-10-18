package com.example.code.exoplayer.styled.ui.vm

import android.content.Context
import android.webkit.URLUtil.isHttpUrl
import android.webkit.URLUtil.isHttpsUrl
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code.exoplayer.Constants.dashUrl
import com.example.code.exoplayer.styled.core.StyledExoPlayer
import com.example.code.exoplayer.styled.ui.viewAction.ExoPlayerAction
import com.example.code.exoplayer.styled.util.MplTrack
import com.example.code.exoplayer.styled.util.MplTrackList
import com.example.code.exoplayer.styled.util.MplVideo
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StyledExoPlayerViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val player : StyledExoPlayer
) : ViewModel() {

    private val tag = this.javaClass.simpleName
    private lateinit var mplVideo: MplVideo
    val command: MutableLiveData<ExoPlayerAction> = MutableLiveData()

    init { setMplVideo() }

    private fun setMplVideo() {
        this.mplVideo = MplVideo(url = dashUrl,
                                sourceType = MplVideo.SOURCE_TYPE_DASH,
                                isVOD = true,startPosition=0)
    }

    private fun initExoPlayer(){

        if(validateUrl(mplVideo.url)){
            player.setListeners(eventListener, analyticsListener)
            player.initPlayer(mplVideo)
        }

    }

    fun pauseVideo() {
        player.pauseVideo()
    }

    fun restartPlayer() {
        player.reInitPlayer()
    }

    fun updateProgressBar() {
        onPlaybackStateChangedListener(player.player?.playbackState)
    }

    fun seekPlayerTo(position: Long){
        viewModelScope.launch {
            player.player?.seekTo(position)
        }
    }


    fun validateUrl(url: String?): Boolean {
        Timber.tag(tag).d("validateUrl $url");
        val isValidUrl: Boolean = isHttpUrl(url) || isHttpsUrl(url)
        command.postValue(ExoPlayerAction.Validation(isSuccess = isValidUrl, url = url))
        return isValidUrl
    }



    private val eventListener: StyledExoPlayer.EventListener = object : StyledExoPlayer.EventListener {

        override fun onPlaybackStateChanged(state: Int) {
            Timber.tag(tag).d("onPlaybackStateChanged")
            onPlaybackStateChangedListener(state)
        }

        override fun onLiveStateChanged(isInSyncWithLive: Boolean, hasEndTag: Boolean) {
            Timber.tag(tag).d("onLiveStateChanged")
        }

        override fun changeForwardIconVisibility(visible: Boolean) {
            Timber.tag(tag).d("changeForwardIconVisibility")
        }

        override fun onTracksChanged(tracksList: MplTrackList) {
            Timber.tag(tag).d("onTracksChanged : $tracksList")
        }

        override fun onTracksSelected(success: Boolean, isSeamless: Boolean, startTime: Long, mplTrack: MplTrack) {
            Timber.tag(tag).d("onTracksSelected")
        }

        override fun onInitDone() {
            Timber.tag(tag).d("onInitDone")
        }

        override fun onError(type: Int, error : String?, currentPosition: Long?) {
            Timber.tag(tag).d("onError")
        }

    }


    private val analyticsListener = object : StyledExoPlayer.CtAnalyticsListener {
        override fun onPause() {
            Timber.tag(tag).d("onPause")
        }

        override fun onPlay() {
            Timber.tag(tag).d("onPlay")
        }

        override fun onFirsFrameRendered() {
            Timber.tag(tag).d("onFirsFrameRendered")
        }

        override fun sendBroadcastViewedEvent() {
            Timber.tag(tag).d("sendBroadcastViewedEvent")
        }
    }

    fun onPlaybackStateChangedListener(state: Int?) {
        Timber.tag(tag).d("onPlaybackStateChangedListener")
        viewModelScope.launch {
            when (state) {
                Player.STATE_BUFFERING -> {
                    Timber.tag(tag).d("Event -> BUFFERING")
                    command.value = ExoPlayerAction.Progressbar(isVisible = true)
                    command.value = ExoPlayerAction.ShowReplay(show = false)
                    command.value = ExoPlayerAction.ShowPlayPause(show = false)
                }

                Player.STATE_ENDED -> {
                    Timber.tag(tag).d("Event -> ENDED")
                    command.value = ExoPlayerAction.Progressbar(isVisible = false)
                    command.value = ExoPlayerAction.ShowReplay(show = true)
                    command.value = ExoPlayerAction.SetForwardIconVisibility(visible = false)
                    command.value = ExoPlayerAction.ShowPlayPause(show = false)
                }

                Player.STATE_IDLE -> {
                    Timber.tag(tag).d("Event -> IDLE")
                    command.value = ExoPlayerAction.ShowReplay(show = false)
                }

                Player.STATE_READY -> {
                    Timber.tag(tag).d("Event -> READY")
                    command.value = ExoPlayerAction.Progressbar(isVisible = false)
                    command.value = ExoPlayerAction.ShowReplay(show = false)
                    command.value = ExoPlayerAction.ShowPlayPause(show = true)
                }
            }
        }
    }


}