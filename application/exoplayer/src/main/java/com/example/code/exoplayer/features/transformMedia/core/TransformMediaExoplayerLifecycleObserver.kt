package com.example.code.exoplayer.features.transformMedia.core

import android.content.Context
import android.os.Environment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.code.exoplayer.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.transformer.Transformer
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import timber.log.Timber
import java.io.File
import java.lang.Exception

class TransformMediaExoplayerLifecycleObserver (
    private val lifecycle: Lifecycle,
    private val context : Context,
    private val callback: (TransformMediaExoplayerAction) -> Unit) : LifecycleObserver, Player.Listener {

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

    private fun initializePlayer(
        url: String= Constants.dashUrl,
        type: String= MimeTypes.APPLICATION_MPD
    ) {
        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        simpleExoplayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->

                callback.invoke(TransformMediaExoplayerAction.BindCustomExoplayer(exoPlayer))

                val mediaItem = MediaItem.Builder()
                    .setUri(url)
                    .setMimeType(type)
                    .build()

                val mime = MimeTypes.VIDEO_MP4
                val fileExtension = ".mp4"
                val fileName = "test"

                val file : File = getFileToBeWritten(context=context, fileName = fileName,
                                                    extension = fileExtension)

                val transformer: Transformer = Transformer.Builder()
                    .setContext(context)
                    .setRemoveAudio(true)
                    .setOutputMimeType(mime)
                    .setListener(transformerListener)
                    .build()

                transformer.startTransformation(mediaItem, file.absolutePath);

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
            callback.invoke(TransformMediaExoplayerAction.ProgressBarVisibility(true))
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            callback.invoke(TransformMediaExoplayerAction.ProgressBarVisibility(false))
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


    private var transformerListener: Transformer.Listener = object : Transformer.Listener {
        override fun onTransformationCompleted(mediaItem: MediaItem) {
            simpleExoplayer?.let{
                it.setMediaItem(mediaItem)
                it.seekTo(currentWindow, playbackPosition)
                it.play()
            }
        }

        override fun onTransformationError(inputMediaItem: MediaItem, e: Exception) {
            Timber.tag(tag).e("ERROR: ${e.message}");
        }
    }

    private fun getFileToBeWritten(context: Context,
                                   fileName:String,
                                   extension:String): File {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName.plus(extension))
        if (!file.exists()) file.parentFile.mkdir()
        return file
    }


}
