package com.example.locationtracker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import com.example.locationtracker.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                navigateToNextScreen()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    Toast.makeText(this, "Permission denied, please grant location permission", Toast.LENGTH_SHORT).show()
                } else {
                    // If the user has denied the permission with "Don't ask again", redirect to settings
                    Toast.makeText(this, "Permission denied permanently, go to settings to grant location permission", Toast.LENGTH_LONG).show()
                    openAppSettings()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun navigateToNextScreen(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                startActivity(Intent(this, GetStartActivity::class.java))
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun navigateToNextScreen() {
        // Navigate to the next screen after permission is granted
        println("Permission granted, navigating to next screen")
        val intent = Intent(this, GetStartActivity::class.java)
        startActivity(intent)
    }

    private fun openAppSettings() {
        // Redirect to the app settings page for the user to manually enable the permission
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    fun backButton(view: View) {
        finish()
    }
}
