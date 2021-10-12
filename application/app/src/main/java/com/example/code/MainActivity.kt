package com.example.code

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.code.Constants.MEDIA_SOURCE_DASH
import com.example.code.Constants.MEDIA_SOURCE_MP4
import com.example.code.Constants.dashUrl
import com.example.code.Constants.mp4Url
import com.example.code.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), Player.Listener {

    private lateinit var binding: ActivityMainBinding


    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0

    private val urlList = listOf(mp4Url to "default", dashUrl to "dash")

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return if (type == "dash") {
            DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        // Url from which video is playing
        val uri = Uri.parse(videoUrl)
        // Determining the type of media source
        val mediaSource = buildMediaSource(uri, type)
        // Apply the params to the exo player
        simpleExoplayer.apply {
            setMediaSource(mediaSource)
            prepare()
        }
    }

    private fun releasePlayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            binding.progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            binding.progressBar.visibility = View.INVISIBLE
    }


    private fun selectUrl(context: Context){
        // setup alert builder
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(getString(R.string.choose_video_format_to_play))

        // add list items
        val listItems = resources.getStringArray(R.array.videoFormatsSelectionList)

        builder.setItems(listItems) { dialog, which ->
            when (which) {
                0 -> initPlayer(mp4Url,MEDIA_SOURCE_MP4)
                1 -> initPlayer(dashUrl, MEDIA_SOURCE_DASH)
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

    /**
     *
     */
    private fun initializePlayer(
        url: String=mp4Url,
        type: String=MEDIA_SOURCE_MP4
    ) {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        preparePlayer(url, type)
        binding.exoplayerView.player = simpleExoplayer
        // Set to initial position
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
    }

}