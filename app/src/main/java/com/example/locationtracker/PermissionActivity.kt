package com.example.locationtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.locationtracker.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionBinding

    // Register for activity result to request the permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // If permission is granted, navigate to the next screen
                navigateToNextScreen()
            } else {
                // If permission is denied, show a toast message
                Toast.makeText(this, "Permission denied, please grant location permission", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun navigateToNextScreen(view: View) {
        // Check if location permission is granted
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                // If permission is granted, navigate to next screen
                startActivity(Intent(this, GetStartActivity::class.java))
            }
            else -> {
                // If permission is not granted, request it
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun navigateToNextScreen() {
        // If permission is granted, proceed to the next screen

       println("permission clicked")
        Toast.makeText(this, "Permission denied, please grant location permission", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, GetStartActivity::class.java)
        startActivity(intent)
    }

    fun backButton(view: View) {
        finish()
    }
}
