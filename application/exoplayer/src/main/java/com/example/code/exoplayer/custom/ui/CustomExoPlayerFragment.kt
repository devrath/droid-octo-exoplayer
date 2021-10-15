package com.example.code.exoplayer.custom.ui

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.code.exoplayer.custom.core.CustomExoplayerAction
import com.example.code.exoplayer.custom.core.CustomExoplayerLifecycleObserver
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentCustomExoPlayerBinding
import com.example.code.extensions.hide
import com.example.code.extensions.show

@AndroidEntryPoint
class CustomExoPlayerFragment : Fragment(), Player.Listener, CustomPlayerCallback {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentCustomExoPlayerBinding.inflate(layoutInflater)
    }

   /* private val bindingCtrl by lazy {
        LayoutExoplayerControlViewsBinding.inflate(layoutInflater, binding.root, true)
    }
    */

    private lateinit var locationListener: CustomExoplayerLifecycleObserver

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
        ExoPlayerContentSelFragment().let {
            it.setOnClickListener(this@CustomExoPlayerFragment)
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
        setClickListener()
    }

    fun playbackSpeedOnClick(v: View?) {
        activity?.let { context ->
            view?.let { playView ->
                val popup = PopupMenu(context, playView)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.playback_speed, popup.menu)
                popup.show()

                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.half_x -> {
                            //player?.playbackParameters = PlaybackParameters(0.5f)
                            true
                        }
                        R.id.one_x -> {
                            // player?.playbackParameters = PlaybackParameters(1f)
                            true
                        }
                        R.id.two_x -> {
                            //player?.playbackParameters = PlaybackParameters(2f)
                            true
                        }
                        R.id.three_x -> {
                            //player?.playbackParameters = PlaybackParameters(3f)
                            true
                        }
                        else -> {
                            //Toast.makeText(this, "Invalid option ", Toast.LENGTH_LONG).show()
                            true
                        }
                    }
                }
            }
        }
    }


    private fun setClickListener() {
       /* findViewById<TextView>(R.id.bindingCtrl)
        activity?.let{ activity ->

            bindingCtrl.toggleInfoIm.setOnClickListener {
                val popup = PopupMenu(activity, it)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.playback_speed, popup.menu)
                popup.show()

                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.half_x -> {
                            //player?.playbackParameters = PlaybackParameters(0.5f)
                            true
                        }
                        R.id.one_x -> {
                           // player?.playbackParameters = PlaybackParameters(1f)
                            true
                        }
                        R.id.two_x -> {
                            //player?.playbackParameters = PlaybackParameters(2f)
                            true
                        }
                        R.id.three_x -> {
                            //player?.playbackParameters = PlaybackParameters(3f)
                            true
                        }
                        else -> {
                            //Toast.makeText(this, "Invalid option ", Toast.LENGTH_LONG).show()
                            true
                        }
                    }
                }
            }
        }*/
    }

    override fun onClick(url: String, type: String) {
        locationListener.changeTrack(url,type)
    }

    private fun initExoplayerListener() {
        activity?.let{
            locationListener = CustomExoplayerLifecycleObserver(lifecycle,it) { exoPlayerAction ->
                when(exoPlayerAction) {
                    is CustomExoplayerAction.BindCustomExoplayer -> binding.exoplayerView.player = exoPlayerAction.simpleExoplayer
                    is CustomExoplayerAction.ProgressBarVisibility -> handleProgressVisibilityOfPlayer(exoPlayerAction.isVisible)
                }
            }
        }
    }

    private fun handleProgressVisibilityOfPlayer(visible: Boolean) {
        if (visible) { binding.progressBar.show() } else { binding.progressBar.hide() }
    }

}