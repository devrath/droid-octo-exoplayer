package com.example.code.exoplayer.styled.ui.view

import android.content.Context
import android.os.Bundle
import android.view.*
import com.example.code.exoplayer.databinding.FragmentStyledExoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StyledExoPlayerFragment : BaseFragment<FragmentStyledExoPlayerBinding>(FragmentStyledExoPlayerBinding::inflate) {

    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}