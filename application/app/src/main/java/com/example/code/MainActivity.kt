package com.example.code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.code.databinding.ActivityMainBinding
import dagger.hilt.EntryPoint

@EntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}