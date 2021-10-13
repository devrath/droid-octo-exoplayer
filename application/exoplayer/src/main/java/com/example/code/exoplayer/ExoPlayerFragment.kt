package com.example.code.exoplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.code.exoplayer.databinding.ActivityExoplayerBinding
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExoPlayerFragment : Fragment(), Player.Listener {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExoplayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: ExoplayerLifecycleObserver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        initExoplayerListener()
    }

    private fun setOnClickListener() {
        binding.selectUrlId.setOnClickListener {
            activity?.let{ selectUrl(it) }
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
                0 -> locationListener.changeTrack(Constants.mp4Url, MimeTypes.APPLICATION_MP4)
                1 -> locationListener.changeTrack(Constants.dashUrl, MimeTypes.APPLICATION_MPD)
                2 -> locationListener.changeTrack(Constants.mp3Url, MimeTypes.APPLICATION_MP4)
                else -> Toast.makeText(context,"Invalid", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        // create & show alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun initExoplayerListener() {
        activity?.let{
            locationListener = ExoplayerLifecycleObserver(lifecycle,it) {
                when(it) {
                    is ExoplayerAction.BindExoplayer -> binding.exoplayerView.player = it.simpleExoplayer
                    is ExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(it.isVisible)
                }
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