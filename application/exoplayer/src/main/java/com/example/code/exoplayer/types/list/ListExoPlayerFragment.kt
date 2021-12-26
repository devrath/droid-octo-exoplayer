package com.example.code.exoplayer.types.list

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import android.widget.Toast
import com.example.code.exoplayer.R
import com.example.code.exoplayer.databinding.FragmentListExoPlayerBinding
import com.example.code.exoplayer.databinding.FragmentSimpleExoPlayerBinding
import com.example.code.exoplayer.types.simple.core.SimpleExoplayerAction
import com.example.code.exoplayer.types.simple.core.SimpleExoplayerLifecycleObserver
import com.example.code.exoplayer.types.simple.ui.SimplePlayerCallback
import com.example.code.exoplayer.util.ToggleFullScreen
import com.example.code.extensions.hide
import com.example.code.extensions.show


@AndroidEntryPoint
class ListExoPlayerFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentListExoPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { return binding.root }

}