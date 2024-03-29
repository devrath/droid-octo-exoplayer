package com.example.code

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.code.databinding.ActivitySelectionBinding
import com.example.code.exoplayers.*
import com.example.code.extensions.launchActivity

class SelectionActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySelectionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.apply {
            simpleExoPlayerId.setOnClickListener {
                launchActivity<SimpleExoPlayerActivity>()
            }
            customExoPlayerId.setOnClickListener {
                launchActivity<CustomExoPlayerActivity>()
            }
            styledExoPlayerId.setOnClickListener {
                launchActivity<StyledExoPlayerActivity>()
            }
            addsExoPlayerId.setOnClickListener {
                launchActivity<AddsExoPlayerActivity>()
            }
            playlistExoPlayerId.setOnClickListener {
                launchActivity<PlaylistExoPlayerActivity>()
            }
            transformMediaId.setOnClickListener {
                launchActivity<TransformMediaExoPlayerActivity>()
            }
            trackSelectionId.setOnClickListener {
                launchActivity<TrackSelectionExoPlayerActivity>()
            }
            retrieveMetaDataId.setOnClickListener {
                launchActivity<RetrievingMetaDataActivity>()
            }
        }
    }

}