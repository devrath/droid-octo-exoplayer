package com.example.code.exoplayer.styled.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.code.exoplayer.databinding.FragmentStyledExoPlayerBinding
import com.example.code.exoplayer.styled.customviews.CustomStyledPlayerView
import com.example.code.exoplayer.styled.ui.viewAction.ExoPlayerAction
import com.example.code.exoplayer.styled.ui.vm.StyledExoPlayerViewModel
import com.example.code.extensions.setVisible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StyledExoPlayerFragment : Fragment() {

    private lateinit var mContext: Context
    private val TAG = this.javaClass.simpleName
    private val viewModel: StyledExoPlayerViewModel by viewModels()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStyledExoPlayerBinding.inflate(layoutInflater)
    }

    /** ************************** LIFE CYCLE METHODS ************************** **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View { return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObservers()
        addClickListeners()
        updateContentUI(showProgress = false, showError = false)
        viewModel.initExoPlayer()
    }

    override fun onStart() {
        super.onStart()
        binding.exoplayerView.playerCtrlBinding.mplLiveSeekbar.onUIControllerStart()
    }

    override fun onStop() {
        super.onStop()
        binding.exoplayerView.playerCtrlBinding.mplLiveSeekbar.onUIControllerStop()
    }

    private fun registerObservers() {
        Timber.tag(TAG).d("Observers are registered !")
        viewModel.command.observe(viewLifecycleOwner, {
            Timber.tag(TAG).d("OnChange $it")
            when (it) {
                is ExoPlayerAction.Validation -> validation(it)
                is ExoPlayerAction.DismissVideoQualityBottomSheet ->
                    Timber.tag(TAG).d("DismissVideoQualityBottomSheet")
                is ExoPlayerAction.EnableTracks -> Timber.tag(TAG).d("EnableTracks")
                is ExoPlayerAction.FirstFrameRendered ->
                    Timber.tag(TAG).d("FirstFrameRendered")
                is ExoPlayerAction.LiveView -> Timber.tag(TAG).d("LiveView")
                is ExoPlayerAction.Progressbar -> Timber.tag(TAG).d("Progressbar")
                is ExoPlayerAction.SelectedTrack -> Timber.tag(TAG).d("SelectedTrack")
                is ExoPlayerAction.SetForwardIconVisibility ->
                    Timber.tag(TAG).d("SetForwardIconVisibility")
                is ExoPlayerAction.ShowError -> {
                    updateContentUI(showProgress = false, showError = true)
                }
                is ExoPlayerAction.ShowPlayPause -> Timber.tag(TAG).d("ShowPlayPause")
                is ExoPlayerAction.ShowReplay -> Timber.tag(TAG).d("ShowReplay")
                is ExoPlayerAction.ShowSnackBar -> Timber.tag(TAG).d("ShowSnackBar")
                is ExoPlayerAction.TracksChange -> Timber.tag(TAG).d("TracksChange")
            }
        })

        viewModel.exoPlayerLiveData.observe(viewLifecycleOwner, { it ->
            Timber.tag(TAG).d("AddLifecycleToExoPlayer ${it.player}")
            lifecycle.addObserver(it)
            it.player?.let {
                Timber.tag(TAG).d("Exoplayer setPlayer")
                binding.exoplayerView.setPlayer(it)
            }
        })
    }

    private fun updateContentUI(showProgress: Boolean, showError: Boolean) {
        binding.apply {
            progressBar.visibility = View.GONE
            exoplayerView.setVisible(!showProgress && !showError)
        }
    }

    private fun validation(it: ExoPlayerAction.Validation) {
        Timber.tag(TAG).d("Valid URL: $it.isSuccess")
    }


    private fun addClickListeners() {

        binding.exoplayerView.apply {
            Timber.tag(TAG).d("Exo player click listeners are set")

            setOnCloseClickListener {
                Timber.tag(TAG).d("close cross icon clicked")
            }

            setOnFullScreenClickListener {
                Timber.tag(TAG).d("Fullscreen clicked")
            }

            setOnScreenRotateClickListener {
                Timber.tag(TAG).d("Rotate screen clicked")
            }

            setOnQualityChangeClickListener {
                Timber.tag(TAG).d("quality change clicked")
            }

            setOnReplayClickListener {
                Timber.tag(TAG).d("Replay clicked")
                binding.exoplayerView.apply {
                    showPlayPauseIcon(true)
                    showReplayIcon(false)
                }

                binding.progressBar.setVisible(true)
                viewModel.seekPlayerTo(0)
            }



            setCallback(object : CustomStyledPlayerView.Callback{
                override fun onPlayWhenReady(playWhenReady: Boolean) {
                    Timber.tag(TAG).d("onPlayWhenReady")
                }

                override fun onFastForward(startTime: Long, targetTime: Long) {
                    Timber.tag(TAG).d("onFastForward")
                }

                override fun onRewind(startTime: Long, targetTime: Long) {
                    Timber.tag(TAG).d("onRewind")
                }

                override fun onSeek(startTime: Long, targetTime: Long) {
                    Timber.tag(TAG).d("onSeek")
                }
            })
        }

    }

}