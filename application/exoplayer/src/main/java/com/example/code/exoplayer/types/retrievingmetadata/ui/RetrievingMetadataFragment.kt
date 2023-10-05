package com.example.code.exoplayer.types.retrievingmetadata.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentRetrieveMetaDataExoPlayerBinding
import com.example.code.exoplayer.types.retrievingmetadata.core.RetrievingMetadataExoplayerAction
import com.example.code.exoplayer.types.retrievingmetadata.core.RetrievingMetadataExoplayerLifecycleObserver
import com.example.code.exoplayer.types.simple.ui.ExoPlayerContentSelFragment
import com.example.code.exoplayer.util.ToggleFullScreen
import com.example.code.extensions.hide
import com.example.code.extensions.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RetrievingMetadataFragment : Fragment() , SimplePlayerCallback{

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentRetrieveMetaDataExoPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: RetrievingMetadataExoplayerLifecycleObserver

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
        RetrievingMetafataSelFragment().let {
            it.setOnClickListener(this@RetrievingMetadataFragment)
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
            locationListener = RetrievingMetadataExoplayerLifecycleObserver(lifecycle,it) { exoPlayerAction ->
                when(exoPlayerAction) {
                    is RetrievingMetadataExoplayerAction.BindCustomExoplayer -> binding.exoplayerView.player = exoPlayerAction.simpleExoplayer
                    is RetrievingMetadataExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(exoPlayerAction.isVisible)
                }
            }
        }
    }

    private fun handleProgressVisibilityOfPlayer(visible: Boolean) {
        if (visible) { binding.progressBar.show() } else { binding.progressBar.hide() }
    }


}