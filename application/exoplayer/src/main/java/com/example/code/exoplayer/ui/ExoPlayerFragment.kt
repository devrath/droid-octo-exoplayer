package com.example.code.exoplayer.ui

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.code.exoplayer.core.ExoplayerAction
import com.example.code.exoplayer.core.ExoplayerLifecycleObserver
import com.example.code.exoplayer.databinding.ActivityExoplayerBinding
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import android.widget.Toast
import com.example.code.exoplayer.R


@AndroidEntryPoint
class ExoPlayerFragment : Fragment(), Player.Listener, ExoPlayerContentSelCallback {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExoplayerBinding.inflate(layoutInflater)
    }

    private lateinit var locationListener: ExoplayerLifecycleObserver

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_video_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
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
        val dialog = ExoPlayerContentSelFragment()
        dialog.setOnClickListener(this)
        dialog.show(childFragmentManager, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initExoplayerListener()
    }

    override fun onClick(url: String, type: String) {
        locationListener.changeTrack(url,type)
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