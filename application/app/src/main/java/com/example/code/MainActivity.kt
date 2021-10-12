package com.example.code

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.code.databinding.ActivityMainBinding
import com.example.code.exoplayer.ExoPlayerActivity
import com.google.android.exoplayer2.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.launchPlayerId.setOnClickListener {
            startActivity(Intent(this@MainActivity, ExoPlayerActivity::class.java))
        }
    }

}