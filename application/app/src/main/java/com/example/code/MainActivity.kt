package com.example.code

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.code.Constants.dashUrl
import com.example.code.Constants.mp3Url
import com.example.code.Constants.mp4Url
import com.example.code.databinding.ActivityMainBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), Player.Listener {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var simpleExoplayer: SimpleExoPlayer ? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L


    private val urlList = listOf(mp4Url to "default", dashUrl to "dash")

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.selectUrlId.setOnClickListener {
            selectUrl(this@MainActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        //hideSystemUi()
        if (Util.SDK_INT <= 23 || simpleExoplayer == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
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
            binding.progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            binding.progressBar.visibility = View.INVISIBLE
    }

    private fun printPlayerState(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
            else -> "UNKNOWN_STATE"
        }
        Log.d(TAG, "changed state to $stateString")
    }

    private fun selectUrl(context: Context){
        // setup alert builder
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(getString(R.string.choose_video_format_to_play))

        // add list items
        val listItems = resources.getStringArray(R.array.videoFormatsSelectionList)

        builder.setItems(listItems) { dialog, which ->
            when (which) {
                0 -> initPlayer(mp4Url, MimeTypes.APPLICATION_MP4)
                1 -> initPlayer(dashUrl,MimeTypes.APPLICATION_MPD)
                2 -> initPlayer(mp3Url,MimeTypes.APPLICATION_MP4)
                else -> Toast.makeText(context,"Invalid",Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        // create & show alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun initPlayer(url: String,type: String) {
        releasePlayer()
        initializePlayer(url,type)
    }

    private fun initializePlayer(
        url: String=mp4Url,
        type: String=MimeTypes.APPLICATION_MP4
    ) {
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        simpleExoplayer = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                binding.exoplayerView.player = exoPlayer

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

}