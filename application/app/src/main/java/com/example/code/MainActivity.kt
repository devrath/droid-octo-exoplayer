package com.example.code

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.code.Constants.dashUrl
import com.example.code.Constants.mp4Url
import com.example.code.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), Player.EventListener {

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

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        val randomUrl = urlList.random()
        preparePlayer(randomUrl.first, randomUrl.second)
        binding.exoplayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
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
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer.prepare(mediaSource)
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


    fun selectUrl(context: Context){
        // setup alert builder
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Choose an Item")

        // add list items
        val listItems = arrayOf("Mp4-URL","Dash-URL","Item 2")
        builder.setItems(listItems) { dialog, which ->
            when (which) {
                0 ->{
                    initPlayer(mp4Url)
                    Toast.makeText(context,"You Clicked Item 0",Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
                1->{
                    initPlayer(dashUrl)
                    Toast.makeText(context,"You Clicked Item 1",Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
                else -> Toast.makeText(context,"Invalid",Toast.LENGTH_LONG).show()
            }
        }

        // create & show alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun initPlayer(url: String) {
        releasePlayer()
        initializePlayer(url)
    }

    private fun initializePlayer(url: String) {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        val randomUrl = urlList.random()
        preparePlayer(randomUrl.first, randomUrl.second)
        binding.exoplayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
    }

}