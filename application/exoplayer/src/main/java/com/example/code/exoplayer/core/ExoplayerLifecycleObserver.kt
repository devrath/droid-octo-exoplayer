package com.example.code.exoplayer.core

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.code.exoplayer.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util

class ExoplayerLifecycleObserver (
    private val lifecycle: Lifecycle,
    private val context : Context,
    private val callback: (ExoplayerAction) -> Unit) : LifecycleObserver, Player.Listener {

    private val TAG = this.javaClass.simpleName

    private var simpleExoplayer: SimpleExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    init { lifecycle.addObserver(this) }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateEvent() {
        Log.i(TAG, "ON_CREATE Event")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartEvent() {
        Log.i(TAG, "ON_START event")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeEvent() {
        Log.i(TAG, "ON_RESUME event")
        if (Util.SDK_INT <= 23 || simpleExoplayer == null) {
            initializePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseEvent() {
        Log.i(TAG, "ON_PAUSE event")
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopEvent() {
        Log.i(TAG, "ON_STOP event")
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyEvent() {
        Log.i(TAG, "ON_DESTROY event")
        lifecycle.removeObserver(this)
    }

    private fun initializePlayer(
        url: String= Constants.mp4Url,
        type: String= MimeTypes.APPLICATION_MP4
    ) {
        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        simpleExoplayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->

                callback.invoke(ExoplayerAction.BindExoplayer(exoPlayer))

                val mediaItem = MediaItem.Builder()
                    .setUri(url)
                    .setMimeType(type)
                    .build()

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        simpleExoplayer?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        simpleExoplayer = null
    }


    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        printPlayerState(playbackState)
        if (playbackState == Player.STATE_BUFFERING)
            callback.invoke(ExoplayerAction.ProgressBarVisibility(true))
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            callback.invoke(ExoplayerAction.ProgressBarVisibility(false))
    }

    private fun printPlayerState(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
            else -> "UNKNOWN_STATE"
        }
        Log.d(ContentValues.TAG, "changed state to $stateString")

    }


    fun changeTrack(url: String,type: String) {
        releasePlayer()
        initializePlayer(url,type)
    }

}
