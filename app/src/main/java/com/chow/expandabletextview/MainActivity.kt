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
            text = "1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950"
            textSize = 25
            textColor = Color.RED
            ellipsizedTextColor = Color.BLUE
            ellipsizedText = "...Whatsup!"
            lineToEllipsize = 3
        }
    }
}