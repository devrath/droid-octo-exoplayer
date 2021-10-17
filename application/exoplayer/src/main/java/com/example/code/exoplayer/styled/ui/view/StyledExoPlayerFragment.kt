package com.example.code.exoplayer.styled.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.code.exoplayer.databinding.FragmentAddsExoPlayerBinding
import com.example.code.exoplayer.databinding.FragmentStyledExoPlayerBinding
import com.example.code.exoplayer.styled.ui.vm.StyledExoPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StyledExoPlayerFragment : Fragment() {

    private lateinit var mContext : Context

    private val viewModel : StyledExoPlayerViewModel by viewModels()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStyledExoPlayerBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}