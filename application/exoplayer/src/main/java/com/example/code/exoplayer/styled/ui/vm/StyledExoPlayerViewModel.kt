package com.example.code.exoplayer.styled.ui.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class StyledExoPlayerViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    fun test(): String {
        return "Hello"
    }


}