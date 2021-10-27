package com.example.code.exoplayer.segmented.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentSegmentedExoPlayerBinding
import com.example.code.exoplayer.databinding.FragmentSimpleExoPlayerBinding
import com.example.code.exoplayer.simple.core.SimpleExoplayerAction
import com.example.code.exoplayer.simple.core.SimpleExoplayerLifecycleObserver
import com.example.code.exoplayer.simple.ui.ExoPlayerContentSelFragment
import com.example.code.exoplayer.simple.ui.SimplePlayerCallback
import com.example.code.exoplayer.util.ToggleFullScreen
import com.example.code.extensions.hide
import com.example.code.extensions.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SegmentedExoPlayerFragment  : Fragment(), SegmentedPlayerCallback {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSegmentedExoPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: SimpleExoplayerLifecycleObserver

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
                showUrlSelectionSheet()
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

    private fun showUrlSelectionSheet() {
        SegmentedExoPlayerSelFragment().let {
            it.setOnClickListener(this@SegmentedExoPlayerFragment)
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
            locationListener = SimpleExoplayerLifecycleObserver(lifecycle,it) { exoPlayerAction ->
                when(exoPlayerAction) {
                    is SimpleExoplayerAction.BindCustomExoplayer -> binding.exoplayerView.player = exoPlayerAction.simpleExoplayer
                    is SimpleExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(exoPlayerAction.isVisible)
                }
            }
        }
    }

    private fun handleProgressVisibilityOfPlayer(visible: Boolean) {
        if (visible) { binding.progressBar.show() } else { binding.progressBar.hide() }
    }

}