package com.example.code.exoplayer.features.trackselection.ui

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentTrackSelectionExoPlayerBinding
import com.example.code.exoplayer.features.trackselection.core.TrackSelectionExoplayerAction
import com.example.code.exoplayer.features.trackselection.core.TrackSelectionExoplayerLifecycleObserver
import com.example.code.extensions.hide
import com.example.code.extensions.show


@AndroidEntryPoint
class TrackSelectionExoPlayerFragment : Fragment(), TrackSelectionCallback {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTrackSelectionExoPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: TrackSelectionExoplayerLifecycleObserver

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_track_selection, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listSelection -> {
                displayTrackListForSelection()
                true
            }
            R.id.printLog -> {
                locationListener.printTrackLogsToExoPlayer()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showUrlSelectionSheet() {
        TrackSelectionBottomSheet().let {
            it.setOnClickListener(this@TrackSelectionExoPlayerFragment)
            it.show(childFragmentManager, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initExoplayerListener()
    }

    override fun onClick(url: String, type: String) {
        locationListener.changeTrack(url,type)
    }

    private fun initExoplayerListener() {
        activity?.let{
            locationListener = TrackSelectionExoplayerLifecycleObserver(lifecycle,it) { exoPlayerAction ->
                when(exoPlayerAction) {
                    is TrackSelectionExoplayerAction.BindCustomExoplayer -> binding.exoplayerView.player = exoPlayerAction.simpleExoplayer
                    is TrackSelectionExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(exoPlayerAction.isVisible)
                }
            }
        }
    }

    private fun handleProgressVisibilityOfPlayer(visible: Boolean) {
        if (visible) { binding.progressBar.show() } else { binding.progressBar.hide() }
    }

    private fun displayTrackListForSelection() {
        val items = locationListener.listVideoTracks()

        TrackSelectionBottomSheet().let {
            it.setOnClickListener(this@TrackSelectionExoPlayerFragment)
            it.setItems(items)
            it.show(childFragmentManager, null)
        }
    }

}