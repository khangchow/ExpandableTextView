package com.chow.expandabletextview

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chow.expandabletextview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tv.apply {
            text = "Hello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello World"
            textSize = 25
            textColor = Color.RED
        }
    }
}