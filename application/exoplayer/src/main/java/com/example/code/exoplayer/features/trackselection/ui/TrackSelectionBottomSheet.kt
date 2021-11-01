package com.example.code.exoplayer.features.trackselection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code.exoplayer.Constants
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentExoPlayerContentSelectionBinding
import com.example.code.exoplayer.databinding.FragmentTrackSelectionBottomSheetBinding
import com.example.code.exoplayer.features.trackselection.adapter.CustomAdapter
import com.example.code.exoplayer.types.simple.ui.SimplePlayerCallback
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class TrackSelectionBottomSheet : BottomSheetDialogFragment(), CoroutineScope {
    //listener?.onClick(Constants.mp3Url, MimeTypes.APPLICATION_MP4)
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var listener : TrackSelectionCallback? = null

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTrackSelectionBottomSheetBinding.inflate(layoutInflater)
    }

    fun setOnClickListener(listener: TrackSelectionCallback) { this.listener = listener }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { return binding.root.apply { setBackgroundResource(R.drawable.rounded_background); } }

    override fun getTheme(): Int { return R.style.CustomBottomSheetDialog }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecyclerView()
        dialog?.setCanceledOnTouchOutside(false)
    }

    private fun initRecyclerView() {

        /*binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        val adapter = CustomAdapter(data)
        binding.recyclerview.adapter = adapter*/

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initListeners() {
        binding.apply {

        }
    }

}

interface TrackSelectionCallback {
    fun onClick(url: String,type: String)
}