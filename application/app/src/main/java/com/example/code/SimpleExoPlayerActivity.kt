package com.example.code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.code.databinding.ActivitySimpleExoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimpleExoPlayerActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySimpleExoPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}