package com.example.code.exoplayer.displayadds.ui

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.code.exoplayer.custom.core.CustomExoplayerAction
import com.example.code.exoplayer.custom.core.CustomExoplayerLifecycleObserver
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import android.widget.Toast
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentSimpleExoPlayerBinding
import com.example.code.exoplayer.displayadds.core.AddsExoplayerAction
import com.example.code.exoplayer.displayadds.core.AddsExoplayerLifecycleObserver
import com.example.code.exoplayer.simple.ui.SimplePlayerCallback
import com.example.code.extensions.hide
import com.example.code.extensions.show


@AndroidEntryPoint
class AddsExoPlayerFragment : Fragment(), Player.Listener, AddsPlayerCallback {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSimpleExoPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: AddsExoplayerLifecycleObserver

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
                Toast.makeText(activity, "Full screen action", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showUrlSelectionSheet() {
        AddsExoPlayerContentSelFragment().let {
            it.setOnClickListener(this@AddsExoPlayerFragment)
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
            locationListener = AddsExoplayerLifecycleObserver(lifecycle,it) { exoPlayerAction ->
                when(exoPlayerAction) {
                    is AddsExoplayerAction.BindCustomExoplayer -> binding.exoplayerView.player = exoPlayerAction.simpleExoplayer
                    is AddsExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(exoPlayerAction.isVisible)
                }
            }
        }
    }

    private fun handleProgressVisibilityOfPlayer(visible: Boolean) {
        if (visible) { binding.progressBar.show() } else { binding.progressBar.hide() }
    }

}