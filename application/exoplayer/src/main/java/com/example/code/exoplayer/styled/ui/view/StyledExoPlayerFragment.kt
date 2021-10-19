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
import com.example.code.exoplayer.styled.ui.viewAction.ExoPlayerAction
import com.example.code.exoplayer.styled.ui.vm.StyledExoPlayerViewModel
import com.example.code.extensions.setVisible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StyledExoPlayerFragment : Fragment() {

    private lateinit var mContext: Context
    private val tAG = this.javaClass.simpleName
    private val viewModel: StyledExoPlayerViewModel by viewModels()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStyledExoPlayerBinding.inflate(layoutInflater)
    }

    /*private val playerCtrlViewBinding by lazy(LazyThreadSafetyMode.NONE) {
        CustomStyledPlayerControlViewBinding.inflate(LayoutInflater.from(context))
    }*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObservers()
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
        viewModel.command.observe(viewLifecycleOwner, {
            Timber.tag(tAG).d("OnChange $it")
            when (it) {
                is ExoPlayerAction.Validation -> validation(it)
                is ExoPlayerAction.DismissVideoQualityBottomSheet ->
                    Timber.tag(tAG).d("DismissVideoQualityBottomSheet")
                is ExoPlayerAction.EnableTracks -> Timber.tag(tAG).d("EnableTracks")
                is ExoPlayerAction.FirstFrameRendered ->
                    Timber.tag(tAG).d("FirstFrameRendered")
                is ExoPlayerAction.LiveView -> Timber.tag(tAG).d("LiveView")
                is ExoPlayerAction.Progressbar -> Timber.tag(tAG).d("Progressbar")
                is ExoPlayerAction.SelectedTrack -> Timber.tag(tAG).d("SelectedTrack")
                is ExoPlayerAction.SetForwardIconVisibility ->
                    Timber.tag(tAG).d("SetForwardIconVisibility")
                is ExoPlayerAction.ShowError -> {
                    updateContentUI(showProgress = false, showError = true)
                }
                is ExoPlayerAction.ShowPlayPause -> Timber.tag(tAG).d("ShowPlayPause")
                is ExoPlayerAction.ShowReplay -> Timber.tag(tAG).d("ShowReplay")
                is ExoPlayerAction.ShowSnackBar -> Timber.tag(tAG).d("ShowSnackBar")
                is ExoPlayerAction.TracksChange -> Timber.tag(tAG).d("TracksChange")
            }
        })

        viewModel.exoPlayerLiveData.observe(viewLifecycleOwner, { it ->
            Timber.tag(tAG).d("AddLifecycleToExoPlayer ${it.player}")
            lifecycle.addObserver(it)
            it.player?.let {
                Timber.tag(tAG).d("Exoplayer setPlayer")
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
        Timber.tag(tag).d("Valid URL: $it.isSuccess")
    }


}