package com.example.code.exoplayer.features.trackselection.core

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.code.exoplayer.Constants
import com.example.code.exoplayer.features.trackselection.model.TrackInfo
import com.example.code.exoplayer.types.styled.util.MplTrack
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import timber.log.Timber


class TrackSelectionExoplayerLifecycleObserver(
    private val lifecycle: Lifecycle,
    private val context: Context,
    private val callback: (TrackSelectionExoplayerAction) -> Unit
) : LifecycleObserver, Player.Listener {

    private val tag = this.javaClass.simpleName

    private var simpleExoplayer: SimpleExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    lateinit var trackSelector: DefaultTrackSelector

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateEvent() {
        Timber.tag(tag).i("ON_CREATE Event");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartEvent() {
        Timber.tag(tag).i("ON_START Event");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeEvent() {
        Timber.tag(tag).i("ON_RESUME Event");
        if (Util.SDK_INT <= 23 || simpleExoplayer == null) {
            initializePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseEvent() {
        Timber.tag(tag).i("ON_PAUSE Event");
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopEvent() {
        Timber.tag(tag).i("ON_STOP Event");
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyEvent() {
        Timber.tag(tag).i("ON_DESTROY Event");
        lifecycle.removeObserver(this)
    }

    private fun initializePlayer(
        url: String = Constants.hls,
        type: String = MimeTypes.APPLICATION_M3U8
    ) {
        trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        simpleExoplayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->

                callback.invoke(TrackSelectionExoplayerAction.BindCustomExoplayer(exoPlayer))

                val mediaItem = MediaItem.Builder()
                    .setUri(url)
                    .setMimeType(type)
                    .build()

                exoPlayer.setMediaItem(mediaItem)
                // This indicates the player that, Player is ready to start and starts playing
                exoPlayer.playWhenReady = playWhenReady
                /**
                 * ---> This is necessary to resume the player from where its paused.
                 * ---> This is useful in scenario where app is sent to background and bought to
                 * foreground the player will resume from position from where is left off
                 * otherwise player will start playing from the initial position */
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        simpleExoplayer?.run {
            playbackPosition = 0L
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        simpleExoplayer = null
    }


    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        printPlayerState(playbackState)
        if (playbackState == Player.STATE_BUFFERING)
            callback.invoke(TrackSelectionExoplayerAction.ProgressBarVisibility(true))
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            callback.invoke(TrackSelectionExoplayerAction.ProgressBarVisibility(false))
    }

    private fun printPlayerState(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
            else -> "UNKNOWN_STATE"
        }
        Timber.tag(tag).i("changed state to $stateString");
    }

    fun changeTrack(url: String, type: String) {
        releasePlayer()
        initializePlayer(url, type)
    }


    fun printTrackLogsToExoPlayer() {
        val mappedTrackInfo = Assertions.checkNotNull(trackSelector.currentMappedTrackInfo)
        val parameters = trackSelector.parameters

        for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
            // ----> Returns the track type in int integer.
            val trackType = mappedTrackInfo.getRendererType(rendererIndex)
            // ----> Returns the name of the track based on track type
            val trackName = trackTypeToName(trackType)
            // ----> Returns the TrackGroups mapped to the renderer at the specified index.
            val trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
            // ----> Returns whether the renderer is disabled.
            val isRendererDisabled = parameters.getRendererDisabled(rendererIndex)
            val selectionOverride = parameters.getSelectionOverride(rendererIndex, trackGroupArray)

            Timber.tag(tag).d("<----- Track item index: $rendererIndex -------------->")
            Timber.tag(tag).d("<----- Track item  name: $trackName ------------------>")
            Timber.tag(tag).d("<----- Is renderer Disabled: $isRendererDisabled ----->")
            Timber.tag(tag).d("<----------------------------------------------------->")
            Timber.tag(tag).d("<----- Selection Override ---------------------------->")
            Timber.tag(tag).d(Gson().toJson(selectionOverride))
            Timber.tag(tag).d("<----- Selection Override ---------------------------->")
            Timber.tag(tag).d("<----------------------------------------------------->")
            Timber.tag(tag).d("<----- Track group Array ----------------------------->")
            Timber.tag(tag).d(Gson().toJson(trackGroupArray))
            Timber.tag(tag).d("<----- Track group Array ----------------------------->")
            Timber.tag(tag).d("<----------------------------------------------------->")

            for (groupIndex in 0 until trackGroupArray.length) {
                for (trackIndex in 0 until trackGroupArray[groupIndex].length) {
                    val trackName = DefaultTrackNameProvider(context.resources).getTrackName(
                        trackGroupArray[groupIndex].getFormat(trackIndex)
                    )
                    val isTrackSupported = mappedTrackInfo.getTrackSupport(
                        rendererIndex, groupIndex, trackIndex) == RendererCapabilities.FORMAT_HANDLED
                    Timber.tag(tag).d("track item $groupIndex: trackName: $trackName, isTrackSupported: $isTrackSupported")
                }
            }
        }

    }

    private fun trackTypeToName(trackType: Int): String {
        return when (trackType) {
            C.TRACK_TYPE_VIDEO -> "TRACK_TYPE_VIDEO"
            C.TRACK_TYPE_AUDIO -> "TRACK_TYPE_AUDIO"
            C.TRACK_TYPE_TEXT -> "TRACK_TYPE_TEXT"
            else -> "Invalid track type"
        }
    }

    fun listVideoTracks(): ArrayList<TrackInfo> {

        trackSelector?.let{
            val tracksForSelection = ArrayList<TrackInfo>()

            val mappedTrackInfo = Assertions.checkNotNull(trackSelector.currentMappedTrackInfo)
            val parameters = trackSelector.parameters

            for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
                // ----> Returns the track type in int integer.
                val trackType = mappedTrackInfo.getRendererType(rendererIndex)
                // ----> Returns the name of the track based on track type
                val trackName = trackTypeToName(trackType)
                // ----> Returns the TrackGroups mapped to the renderer at the specified index.
                val trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
                // ----> Returns whether the renderer is disabled.
                val isRendererDisabled = parameters.getRendererDisabled(rendererIndex)
                val selectionOverride = parameters.getSelectionOverride(rendererIndex, trackGroupArray)

                if(trackName.equals("TRACK_TYPE_VIDEO",true)
                    && !isRendererDisabled ){
                    for (groupIndex in 0 until trackGroupArray.length) {
                        for (trackIndex in 0 until trackGroupArray[groupIndex].length) {
                            val trackName = DefaultTrackNameProvider(context.resources).getTrackName(
                                trackGroupArray[groupIndex].getFormat(trackIndex)
                            )
                            val isTrackSupported = mappedTrackInfo.getTrackSupport(
                                rendererIndex, groupIndex, trackIndex) == C.FORMAT_HANDLED
                            if(isTrackSupported){
                                Timber.tag(tag).d("track item $groupIndex: trackName: $trackName, isTrackSupported: $isTrackSupported")
                            }

                            // Add item
                            tracksForSelection.add(
                                TrackInfo(trackName = trackName, groupIndex = groupIndex, trackIndex = trackIndex)
                            )
                        }
                    }
                }
            }

            return tracksForSelection
        }



        //selectTrack(reason = C.SELECTION_REASON_MANUAL,groupIndex = 0, trackIndex = 2)
    }


    fun selectTrack(reason: Int, groupIndex: Int, trackIndex: Int) {

        trackSelector.currentMappedTrackInfo?.let {mappedTrackInfo ->
            val builder = trackSelector.parameters.buildUpon()

            for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {

                val trackType = mappedTrackInfo.getRendererType(rendererIndex)

                if (trackType == C.TRACK_TYPE_VIDEO) {

                    builder.clearSelectionOverrides(rendererIndex).setRendererDisabled(rendererIndex, false)

                    if (reason == C.SELECTION_REASON_MANUAL) {
                        val override = DefaultTrackSelector.SelectionOverride(groupIndex, trackIndex)
                        builder.setSelectionOverride(rendererIndex, mappedTrackInfo.getTrackGroups(rendererIndex), override)
                    } else {
                        builder.setAllowMultipleAdaptiveSelections(true)
                    }

                }
            }

            trackSelector.setParameters(builder)

        }
    }

}
