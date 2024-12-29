//    package com.example.locationtracker.screens
//
//    import Checker
//    import PermissionHelper
//    import android.annotation.SuppressLint
//    import android.content.Context
//    import android.content.Intent
//    import android.os.Build
//    import android.os.Bundle
//    import android.provider.Settings
//    import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//    import android.util.Log
//    import androidx.annotation.RequiresApi
//    import androidx.appcompat.app.AlertDialog
//    import androidx.appcompat.app.AppCompatActivity
//    import androidx.fragment.app.Fragment
//    import androidx.lifecycle.lifecycleScope
//    import com.example.locationtracker.DeviceNameActivity
//    import com.example.locationtracker.HomeActivity
//    import com.example.locationtracker.databinding.ActivitySplashBinding
//    import com.google.firebase.database.DatabaseReference
//    import com.google.firebase.database.FirebaseDatabase
//    import kotlinx.coroutines.delay
//    import kotlinx.coroutines.launch
//
//    @SuppressLint("CustomSplashScreen")
//    class SplashActivity : AppCompatActivity() {
//        private lateinit var binding: ActivitySplashBinding
//        private lateinit var database: FirebaseDatabase
//        private lateinit var deviceReference: DatabaseReference
//        private lateinit var androidId: String
//
//
//        @SuppressLint("HardwareIds")
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//
//            binding = ActivitySplashBinding.inflate(layoutInflater)
//            setContentView(binding.root)
//
//
//
//            // Initialize Firebase
//            database = FirebaseDatabase.getInstance()
//            deviceReference = database.getReference("devices")
//
//            // Get the device's unique ID
//            androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//            if (androidId.isEmpty()) {
//                Log.e("SplashActivity", "Failed to retrieve Android ID")
//                navigateToGetStarted()
//                return
//            }
//
//            Log.d("SplashActivity", "Device Android ID: $androidId")
//
//            // Check if device details exist in Firebase
//            lifecycleScope.launch {
//                delay(2000) // 2 seconds for splash screen display
//                checkDeviceDetails()
//            }
//        }
//
//        override fun onResume() {
//            super.onResume()
//            checkDeviceDetails()
//
//
//            // Check and request battery optimization exclusion every time the activity resumes
//        }
//
//
//
//        private fun checkDeviceDetails() {
//            Log.d("SplashActivity", "Checking device details for Android ID: $androidId")
//
//            deviceReference.child(androidId).get().addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val snapshot = task.result
//                    Log.d("SplashActivity", "Task successful: ${task.result}")
//                    if (snapshot.exists()) {
//                        Log.d("SplashActivity", "Device details found in Firebase.")
//                        navigateToHome()
//                    } else {
//                        Log.d("SplashActivity", "Device details not found in Firebase.")
//                        navigateToGetStarted()
//                    }
//                } else {
//                    Log.e("SplashActivity", "Error fetching device details: ${task.exception?.message}")
//                    navigateToGetStarted()
//                }
//            }.addOnFailureListener { exception ->
//                Log.e("SplashActivity", "Failed to fetch device details: ${exception.message}")
//                navigateToGetStarted()
//            }
//        }
//
//        private fun navigateToHome() {
//            Log.d("SplashActivity", "Navigating to HomeActivity")
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        private fun navigateToGetStarted() {
//            Log.d("SplashActivity", "Navigating to DeviceNameActivity")
//            val intent = Intent(this, DeviceNameActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//
//
//
//
//    }
package com.example.locationtracker.screens

import Checker
import PermissionHelper
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.locationtracker.DeviceNameActivity
import com.example.locationtracker.HomeActivity
import com.example.locationtracker.databinding.ActivitySplashBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var deviceReference: DatabaseReference
    private lateinit var androidId: String
    private lateinit var checker: Checker
    private lateinit var permissionHelper: PermissionHelper

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checker = Checker(this)
        permissionHelper = PermissionHelper(this)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        deviceReference = database.getReference("devices")

        // Get the device's unique ID
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        if (androidId.isEmpty()) {
            Log.e("SplashActivity", "Failed to retrieve Android ID")
            navigateToGetStarted()
            return
        }

        Log.d("SplashActivity", "Device Android ID: $androidId")

        // Check if device details exist in Firebase
        lifecycleScope.launch {
            delay(2000) // 2 seconds for splash screen display
            checkDeviceDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        if (!checker.isInternetEnabled()) {
            showInternetDisabledDialog()
            return
        }

        if (!checker.isLocationEnabled()) {
            showLocationDisabledDialog()
            return
        }

        if (!checker.isBatteryOptimizationUnrestricted()) {
            showBatteryOptimizationDialog()
            return
        }

        if (!permissionHelper.checkLocationPermissions()) {
            permissionHelper.requestLocationPermissions()
            return
        }

        if (!permissionHelper.checkBackgroundLocationPermission()) {
            permissionHelper.requestBackgroundLocationPermission()
            return
        }

        // If all checks passed, proceed to check device details
        checkDeviceDetails()
    }

    private fun showInternetDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Internet Disabled")
            .setMessage("Internet connection is required for this app to function properly. Please enable internet connectivity.")
            .setPositiveButton("Go to Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLocationDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Disabled")
            .setMessage("Location services are required for this app to function properly. Please enable location services.")
            .setPositiveButton("Go to Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @SuppressLint("BatteryLife")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showBatteryOptimizationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Battery Optimization Required")
            .setMessage("For this app to function properly, it needs to run in the background without battery restrictions. Please go to settings and enable 'Don't optimize' for this app.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = android.net.Uri.parse("package:$packageName")
                startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun checkDeviceDetails() {
        Log.d("SplashActivity", "Checking device details for Android ID: $androidId")

        deviceReference.child(androidId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                Log.d("SplashActivity", "Task successful: ${task.result}")
                if (snapshot.exists()) {
                    Log.d("SplashActivity", "Device details found in Firebase.")
                    navigateToHome()
                } else {
                    Log.d("SplashActivity", "Device details not found in Firebase.")
                    navigateToGetStarted()
                }
            } else {
                Log.e("SplashActivity", "Error fetching device details: ${task.exception?.message}")
                navigateToGetStarted()
            }
        }.addOnFailureListener { exception ->
            Log.e("SplashActivity", "Failed to fetch device details: ${exception.message}")
            navigateToGetStarted()
        }
    }

    private fun navigateToHome() {
        Log.d("SplashActivity", "Navigating to HomeActivity")
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToGetStarted() {
        Log.d("SplashActivity", "Navigating to DeviceNameActivity")
        val intent = Intent(this, DeviceNameActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS = 1
    }
}
