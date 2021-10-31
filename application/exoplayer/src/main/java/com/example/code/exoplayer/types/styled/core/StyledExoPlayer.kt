package com.example.code.exoplayer.types.styled.core

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.code.exoplayer.types.styled.util.MplTrack
import com.example.code.exoplayer.types.styled.util.MplTrackList
import com.example.code.exoplayer.types.styled.util.MplVideo
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.PlaybackStatsListener
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class StyledExoPlayer  @Inject constructor(
    @ApplicationContext val context: Context
)  : LifecycleObserver {

    companion object {
        const val TAG = "ExoPlayer : MplExoPlayer"
        fun getAutoTrack() = MplTrack("Auto", true,-1,-1, true, null)
    }

    private val tag = this.javaClass.simpleName

    var player: SimpleExoPlayer? = null
    private var selectedTrack: MplTrack? = getAutoTrack()
    private var  eventListener: EventListener? = null
    private var  analyticsListener: CtAnalyticsListener? = null
    private var mplVideo: MplVideo? = null
    private var isPlaying: Boolean? = false



    // For track selection -------------------------------- >
    private lateinit var trackSelector: DefaultTrackSelector
    private lateinit var mplAdaptiveTrackSelectionFactory: MplAdaptiveTrackSelection.Factory


    /** ********************************* LIFE CYCLE EVENTS *********************************  **/
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateEvent() {
        Timber.tag(tag).d("ON_CREATE Event");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartEvent() {
        Timber.tag(tag).d("ON_START Event");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeEvent() {
        Timber.tag(tag).d("ON_RESUME Event");
        resumeVideo()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseEvent() {
        Timber.tag(tag).d("ON_PAUSE Event");
        isPlaying = player?.isPlaying
        pauseVideo()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopEvent() {
        Timber.tag(tag).d("ON_STOP Event");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyEvent() {
        Timber.tag(tag).d("ON_DESTROY Event");
        releasePlayer()
    }
    /** ********************************* LIFE CYCLE EVENTS *********************************  **/


    fun pauseVideo() {
        player?.playWhenReady = false
    }

    private fun playVideo() {
        player?.playWhenReady = true
    }

    @Suppress("SENSELESS_COMPARISON")
    fun resumeVideo() {
        Timber.tag(tag).d("resumeVideo");
        if (player == null) {
            reInitPlayer()
        }
        if (isPlaying == true) {
            playVideo()
        }
    }

    fun reInitPlayer() {
        releasePlayer()
        initPlayer(mplVideo)
    }

    private fun releasePlayer() {
        Timber.tag(tag).d("releasePlayer");

        player?.stop()
        player?.release()
        removeListeners()
        player = null
    }

    private fun removeListeners() {
        player?.apply {
            removeListener(playerEventListener)
            removeAnalyticsListener(playerAnalyticsListener)
            removeAnalyticsListener(playbackStatsListener)
        }
    }

    private val playerEventListener: Player.Listener = object : Player.Listener {

        override fun onPlayerError(error: PlaybackException) {
            Timber.tag(tag).d( "onPlayerError - Invoked")

            error.printStackTrace()

            if (isBehindLiveWindow(error)) {
                Timber.tag(tag).e( "isBehindLiveWindowError")
                try {
                    reInitPlayer()
                } catch (e: Exception) {
                    Timber.tag(tag).e( "Error in reInitPlayer : ${e.message}")
                    e.printStackTrace()
                }
            } else {
                handleError(error)
            }
        }

        override fun onPlaybackStateChanged(state: Int) {
            Timber.tag(tag).d("onPlaybackStateChanged - Invoked")
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            Timber.tag(tag).d("onTimelineChanged - Invoked")
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            super.onTracksChanged(trackGroups, trackSelections)
            Timber.tag(tag).d("onTracksChanged - Invoked")
            //val tracksList : MplTrackList = getTracksByType(C.TRACK_TYPE_VIDEO)

        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Timber.tag(tag).d("onIsPlayingChanged - Invoked")
        }

        override fun onPositionDiscontinuity(reason: Int) {
            Timber.tag(tag).d("onPositionDiscontinuity - Invoked")
        }

    }

    private val playerAnalyticsListener : AnalyticsListener = object : AnalyticsListener {

        override fun onLoadStarted(eventTime: AnalyticsListener.EventTime, loadEventInfo: LoadEventInfo, mediaLoadData: MediaLoadData) {
            super.onLoadStarted(eventTime, loadEventInfo, mediaLoadData)
            Timber.tag(tag).d( "onLoadStarted - Invoked")
        }

        override fun onSurfaceSizeChanged(eventTime: AnalyticsListener.EventTime, width: Int, height: Int) {
            super.onSurfaceSizeChanged(eventTime, width, height)
            Timber.tag(tag).d( "onSurfaceSizeChanged - Invoked")
        }

        override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime, output: Any, renderTimeMs: Long) {
            super.onRenderedFirstFrame(eventTime, output, renderTimeMs)
            Timber.tag(tag).d( "onRenderedFirstFrame - Invoked")
        }

    }

    private val playbackStatsListener : PlaybackStatsListener = PlaybackStatsListener(false, null)

    fun initPlayer(mplVideo: MplVideo?) {
        Timber.tag(tag).d("initPlayer - Invoked")

        releasePlayer()

        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter
            .Builder(context)
            .build()

        mplAdaptiveTrackSelectionFactory = MplAdaptiveTrackSelection.Factory()
        trackSelector = DefaultTrackSelector(context, mplAdaptiveTrackSelectionFactory)
        trackSelector.parameters = DefaultTrackSelector.ParametersBuilder(context).build()


        player = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setBandwidthMeter(bandwidthMeter)
            .build()

        addListeners()
        player?.prepare()
        player?.repeatMode = Player.REPEAT_MODE_OFF
        eventListener?.onInitDone()

        setMediaSource(mplVideo)
    }

    private fun addListeners() {
        player?.apply {
            addListener(playerEventListener)
            addAnalyticsListener(playerAnalyticsListener)
            addAnalyticsListener(playbackStatsListener)
        }
    }

    private fun setMediaSource(mplVideo: MplVideo?) {
        Timber.tag(tag).d("setMediaSource -> [VideoUrl : ${mplVideo?.url}, sourceType : ${mplVideo?.sourceType}]")

        this.mplVideo = mplVideo

        if (mplVideo == null) {
            Timber.tag(tag).d("MplVideo is null")
            return
        }

        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()


        if (mplVideo.sourceType == MplVideo.SOURCE_TYPE_HLS) {
            val hlsMediaSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                .setAllowChunklessPreparation(true)
                //.setLoadErrorHandlingPolicy(MplLoadErrorPolicy(gson, exoplayerConfig))
                .createMediaSource(MediaItem.fromUri(mplVideo.url))

            player?.setMediaSource(hlsMediaSource, false)
        } else if (mplVideo.sourceType == MplVideo.SOURCE_TYPE_DASH) {
            val dashMediaSource: DashMediaSource = DashMediaSource.Factory(dataSourceFactory)
                //.setLoadErrorHandlingPolicy(MplLoadErrorPolicy(gson, exoplayerConfig))
                .createMediaSource(MediaItem.fromUri(mplVideo.url))

            player?.setMediaSource(dashMediaSource, false)
        }

        player?.seekToDefaultPosition()

        playVideo()
    }

    private fun isBehindLiveWindow(e: PlaybackException): Boolean {
        Timber.tag(tag).e("Event -> isBehindLiveWindow")
        if (e.errorCode != ExoPlaybackException.TYPE_SOURCE) { return false }
        var cause: Throwable? = e.cause
        while (cause != null) {
            if (cause is BehindLiveWindowException) { return true }
            cause = cause.cause
        }
        return false
    }

    private fun handleError(error : PlaybackException) {
        val errorString = "Message : ${error.message}, Cause : ${error.cause?.message ?: "null"}"
        Timber.tag(tag).e("ExoPlaybackException-$error.errorCodeName")
        eventListener?.onError(error.errorCode, errorString, player?.currentPosition)
    }

    fun setListeners(eventListener: EventListener? = null,
                     analyticsListener: CtAnalyticsListener? = null) {
        this.eventListener = eventListener
        this.analyticsListener = analyticsListener
    }

    interface EventListener {
        fun onInitDone()
        fun onPlaybackStateChanged(state: Int)
        fun onLiveStateChanged(isInSyncWithLive: Boolean, hasEndTag: Boolean)
        fun changeForwardIconVisibility(visible: Boolean)
        fun onTracksChanged(tracksList: MplTrackList)
        fun onTracksSelected(success: Boolean, isSeamless: Boolean, startTime: Long, mplTrack: MplTrack)
        fun onError(type: Int, error : String?, currentPosition: Long?)
    }

    interface CtAnalyticsListener {
        fun onPause()
        fun onPlay()
        fun onFirsFrameRendered()
        fun sendBroadcastViewedEvent()
    }


    fun getTrackSelector(): DefaultTrackSelector {
        return trackSelector
    }
}