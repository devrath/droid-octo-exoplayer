package com.example.code.exoplayer.features.playlist.ui

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import android.widget.Toast
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentPlaylistExoPlayerBinding
import com.example.code.exoplayer.databinding.FragmentSimpleExoPlayerBinding
import com.example.code.exoplayer.features.playlist.core.PlaylistExoplayerAction
import com.example.code.exoplayer.features.playlist.core.PlaylistExoplayerLifecycleObserver
import com.example.code.exoplayer.util.ToggleFullScreen
import com.example.code.extensions.hide
import com.example.code.extensions.show


@AndroidEntryPoint
class PlaylistExoPlayerFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentPlaylistExoPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: PlaylistExoplayerLifecycleObserver

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_video_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_url_selection -> {
                //showUrlSelectionSheet()
                true
            }
            R.id.action_full_Screen -> {
                ToggleFullScreen(activity,view).toggleSystemUI()
                Toast.makeText(activity, "Full screen", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    private fun initExoplayerListener() {
        activity?.let{
            locationListener = PlaylistExoplayerLifecycleObserver(lifecycle,it) { exoPlayerAction ->
                when(exoPlayerAction) {
                    is PlaylistExoplayerAction.BindCustomExoplayer -> binding.exoplayerView.player = exoPlayerAction.simpleExoplayer
                    is PlaylistExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(exoPlayerAction.isVisible)
                }
            }
        }
    }

    private fun handleProgressVisibilityOfPlayer(visible: Boolean) {
        if (visible) { binding.progressBar.show() } else { binding.progressBar.hide() }
    }

}