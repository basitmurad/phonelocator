package com.example.locationtracker.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.locationtracker.R
import com.example.locationtracker.databinding.ActivityShowDetailBinding

class ShowDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

binding = ActivityShowDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}