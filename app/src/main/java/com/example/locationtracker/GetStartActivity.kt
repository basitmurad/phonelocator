package com.example.locationtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.locationtracker.databinding.ActivityGetStartBinding

class GetStartActivity : AppCompatActivity() {
     private lateinit var binding: ActivityGetStartBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun navigateToNextScreen(view: View) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)     }

    fun backButton(view: View) {

        finish()
    }
}