//
//package com.example.locationtracker
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import com.example.locationtracker.databinding.ActivityPermissionBinding
//
//class PermissionActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityPermissionBinding
//
//    // Register for activity result to request the permission
//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                // If permission is granted, navigate to the next screen
//                navigateToNextScreen()
//            } else {
//                // If permission is denied, show a toast message
//                Toast.makeText(this, "Permission denied, please go to app info and grant location permission", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPermissionBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//    }
//
//    fun navigateToNextScreen(view: View) {
//        // Check if location permission is granted
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
//                // If permission is granted, navigate to next screen
//                startActivity(Intent(this, GetStartActivity::class.java))
//            }
//            else -> {
//                // If permission is not granted, request it
//                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//            }
//        }
//    }
//
//    private fun navigateToNextScreen() {
//        // Navigate to the next screen after permission is granted
//        println("Permission granted, navigating to next screen")
//        val intent = Intent(this, GetStartActivity::class.java)
//        startActivity(intent)
//    }
//
//    fun backButton(view: View) {
//        finish()
//    }
//}
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

    // Register for activity result to request the permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // If permission is granted, navigate to the next screen
                navigateToNextScreen()
            } else {
                // If permission is denied, check if the user has permanently denied it
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    // If the user can still be prompted, show a toast
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
