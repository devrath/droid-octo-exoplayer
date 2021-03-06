package com.example.code.exoplayer.features.playlist.core

import android.content.Context
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.code.exoplayer.util.playlists.PlayList.dashItemList
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import timber.log.Timber





class PlaylistExoplayerLifecycleObserver (
    private val lifecycle: Lifecycle,
    private val context : Context,
    private val callback: (PlaylistExoplayerAction) -> Unit) : LifecycleObserver, Player.Listener {

    private val tag = this.javaClass.simpleName

    private var simpleExoplayer: SimpleExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    init { lifecycle.addObserver(this) }

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

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        val errorName =  error.errorCodeName
        Timber.tag(tag).i(errorName);
    }

    private fun initializePlayer() {
        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        simpleExoplayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->

                callback.invoke(PlaylistExoplayerAction.BindCustomExoplayer(exoPlayer))

                val videosList = dashItemList()
                val type: String = MimeTypes.APPLICATION_MPD // -> DASH
                //val type: String = MimeTypes.APPLICATION_M3U8 // -> HLS
                val mediaItems: MutableList<MediaItem> = ArrayList()
                for (i in 0 until videosList.size) {
                    val mediaItem = MediaItem.Builder()
                        // Add the url to be played
                        .setUri(videosList[i].uri)
                        // Set the mime type for playing video
                        .setMimeType(type)
                        .build()
                    mediaItems.add(mediaItem)
                }

                exoPlayer.setMediaItems(mediaItems)
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

        setPlayerMediaTransistion()
    }

    private fun setPlayerMediaTransistion() {
        simpleExoplayer?.addListener(object : Player.Listener { // player listener

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) { // check player play back state
                    Player.MEDIA_ITEM_TRANSITION_REASON_SEEK ->{
                        Timber.tag(tag).i("MEDIA_ITEM_TRANSITION_REASON_SEEK");
                    }
                    Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED ->{
                        Timber.tag(tag).i("MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED");
                    }
                }
            }
        })
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
            callback.invoke(PlaylistExoplayerAction.ProgressBarVisibility(true))
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            callback.invoke(PlaylistExoplayerAction.ProgressBarVisibility(false))
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

    fun changeTrack(url: String,type: String) {
        releasePlayer()
        initializePlayer()
    }

}
