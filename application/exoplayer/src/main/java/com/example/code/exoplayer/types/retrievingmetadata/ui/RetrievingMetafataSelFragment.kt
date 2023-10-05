package com.example.code.exoplayer.types.retrievingmetadata.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.code.exoplayer.Constants
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentExoPlayerContentSelectionBinding
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class RetrievingMetafataSelFragment : BottomSheetDialogFragment(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var listener : SimplePlayerCallback? = null

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentExoPlayerContentSelectionBinding.inflate(layoutInflater)
    }

    fun setOnClickListener(listener: SimplePlayerCallback) { this.listener = listener }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { return binding.root.apply { setBackgroundResource(R.drawable.rounded_background); } }

    override fun getTheme(): Int { return R.style.CustomBottomSheetDialog }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initListeners() {
        binding.apply {
            contentMp3Id.setOnClickListener {
                lifecycleScope.launch {
                    dismissAllowingStateLoss()
                    listener?.onClick(Constants.mp3Url, MimeTypes.APPLICATION_MP4)
                }
            }
            contentMp4Id.setOnClickListener {
                lifecycleScope.launch {
                    dismissAllowingStateLoss()
                    listener?.onClick(Constants.mp4Url, MimeTypes.APPLICATION_MP4)
                }
            }
            contentDashId.setOnClickListener {
                lifecycleScope.launch {
                    dismissAllowingStateLoss()
                    val url = "http://devimages.apple.com/samplecode/adDemo/ad.m3u8";
                    listener?.onClick(url, MimeTypes.APPLICATION_MPD)
                }
            }
        }
    }

}

interface SimplePlayerCallback {
    fun onClick(url: String,type: String)
}