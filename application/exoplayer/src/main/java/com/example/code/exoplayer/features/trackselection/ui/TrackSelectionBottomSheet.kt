package com.example.code.exoplayer.features.trackselection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentTrackSelectionBottomSheetBinding
import com.example.code.exoplayer.features.trackselection.adapter.CustomAdapter
import com.example.code.exoplayer.features.trackselection.model.TrackInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class TrackSelectionBottomSheet : BottomSheetDialogFragment(), CoroutineScope {

    //listener?.onClick(Constants.mp3Url, MimeTypes.APPLICATION_MP4)
    var itemsList =  ArrayList<TrackInfo>()

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
        initRecyclerView()
        dialog?.setCanceledOnTouchOutside(false)
    }

    private fun initRecyclerView() {

        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        val adapter = CustomAdapter(itemsList)
        binding.recyclerview.adapter = adapter

        binding.recyclerview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            var downTouch = false
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> downTouch = true
                    MotionEvent.ACTION_UP -> if (downTouch) {
                        downTouch = false
                        binding.recyclerview.findChildViewUnder(e.x, e.y)?.let {
                            val position = rv.getChildAdapterPosition(it)
                            Toast.makeText(rv.context, "clicked on $position", Toast.LENGTH_SHORT).show()
                            listener?.onClick(itemsList[position])
                            dismiss()
                        }
                    }
                    else -> downTouch = false
                }
                return super.onInterceptTouchEvent(rv, e)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    fun setItems(items: ArrayList<TrackInfo>) {
        itemsList.clear()
        itemsList.addAll(items)
    }

}

interface TrackSelectionCallback {
    fun onClick(info: TrackInfo)
}