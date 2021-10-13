package com.example.code.exoplayer

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.code.exoplayer.Constants.dashUrl
import com.example.code.exoplayer.Constants.mp3Url
import com.example.code.exoplayer.Constants.mp4Url
import com.example.code.exoplayer.databinding.ActivityExoplayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExoPlayerActivity : AppCompatActivity(), Player.Listener {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExoplayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: ExoplayerLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickListener()
        initExoplayerListener()
    }

    private fun setOnClickListener() {
        binding.selectUrlId.setOnClickListener {
            selectUrl(this@ExoPlayerActivity)
        }
    }

    private fun selectUrl(context: Context){
        // setup alert builder
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(getString(R.string.choose_video_format_to_play))

        // add list items
        val listItems = resources.getStringArray(R.array.videoFormatsSelectionList)

        builder.setItems(listItems) { dialog, which ->
            when (which) {
                0 -> locationListener.changeTrack(mp4Url, MimeTypes.APPLICATION_MP4)
                1 -> locationListener.changeTrack(dashUrl, MimeTypes.APPLICATION_MPD)
                2 -> locationListener.changeTrack(mp3Url, MimeTypes.APPLICATION_MP4)
                else -> Toast.makeText(context,"Invalid", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        // create & show alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun initExoplayerListener() {
        locationListener = ExoplayerLifecycleObserver(lifecycle,this) { it ->
            when(it) {
                is ExoplayerAction.BindExoplayer -> binding.exoplayerView.player = it.simpleExoplayer
                is ExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(it.isVisible)
            }
        }
    }


    private fun handleProgressVisibilityOfPlayer(visible: Boolean) {
        if (visible)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.INVISIBLE
    }

}