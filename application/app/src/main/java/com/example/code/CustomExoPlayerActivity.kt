package com.example.code

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.code.databinding.ActivitySimpleExoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomExoPlayerActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySimpleExoPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}