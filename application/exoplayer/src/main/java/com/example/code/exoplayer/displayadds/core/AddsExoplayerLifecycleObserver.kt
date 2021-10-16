package com.example.code.exoplayer.displayadds.core

import android.content.Context
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.code.exoplayer.Constants
import com.example.code.exoplayer.Constants.addUrl
import com.example.code.exoplayer.R
import com.example.code.exoplayer.simple.core.SimpleExoplayerAction
import com.google.ads.interactivemedia.v3.api.AdErrorEvent
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import timber.log.Timber
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory

import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView


class AddsExoplayerLifecycleObserver (
    private val lifecycle: Lifecycle,
    private val context : Context,
    private val playerView : PlayerView,
    private val callback: (AddsExoplayerAction) -> Unit) :
    LifecycleObserver, Player.Listener,
    AdErrorEvent.AdErrorListener,  AdEvent.AdEventListener{

    private val tag = this.javaClass.simpleName

    private var simpleExoplayer: SimpleExoPlayer? = null
    private var adsLoader: ImaAdsLoader? = null

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

    private fun initializePlayer(
        url: String= Constants.mp4Url,
        type: String= MimeTypes.APPLICATION_MP4
    ) {
        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        val adsLoaderProvider : DefaultMediaSourceFactory.AdsLoaderProvider =
            DefaultMediaSourceFactory.AdsLoaderProvider { getAdsLoaderProvider() }

        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(context)
            .setAdsLoaderProvider(adsLoaderProvider)
            .setAdViewProvider(playerView)

        simpleExoplayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .also { exoPlayer ->

                callback.invoke(AddsExoplayerAction.BindCustomExoplayer(exoPlayer))

                val mediaItem = MediaItem.Builder()
                    .setUri(Uri.parse(url))
                    .setAdTagUri(Uri.parse(addUrl))
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
            callback.invoke(AddsExoplayerAction.ProgressBarVisibility(true))
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            callback.invoke(AddsExoplayerAction.ProgressBarVisibility(false))
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
        initializePlayer(url,type)
    }

    private fun getAdsLoaderProvider() : ImaAdsLoader {
        adsLoader = ImaAdsLoader.Builder(context)
            .setAdErrorListener(this)
            .setAdEventListener(this)
            .build()
        adsLoader?.setPlayer(playerView.player)
        return adsLoader as ImaAdsLoader
    }

    override fun onAdEvent(adEvent: AdEvent?) {
        adEvent?.type?.name.let {
            Timber.tag("ADS").i( "AdEvent listener :: $it!!");
        }
    }

    override fun onAdError(adErrorEvent: AdErrorEvent?) {
        adErrorEvent?.error?.message?.let {
            Timber.tag("ADS").i( "AdError listener :: $it!!");
        }
    }

}
