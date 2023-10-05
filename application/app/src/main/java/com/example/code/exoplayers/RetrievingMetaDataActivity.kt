package com.example.code.exoplayers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.code.R
import com.example.code.databinding.ActivityRetrievingMetaDataBinding
import com.example.code.databinding.ActivitySimpleExoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RetrievingMetaDataActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRetrievingMetaDataBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}