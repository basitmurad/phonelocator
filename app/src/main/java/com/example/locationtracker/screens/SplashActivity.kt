//package com.example.locationtracker.screens
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.provider.Settings
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.locationtracker.DeviceNameActivity
//import com.example.locationtracker.HomeActivity
//import com.example.locationtracker.databinding.ActivitySplashBinding
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//@SuppressLint("CustomSplashScreen")
//class SplashActivity : AppCompatActivity() {
//    private lateinit var binding: ActivitySplashBinding
//    private lateinit var database: FirebaseDatabase
//    private lateinit var deviceReference: DatabaseReference
//    private lateinit var androidId: String
//
//    @SuppressLint("HardwareIds")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivitySplashBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Initialize Firebase
//        database = FirebaseDatabase.getInstance()
//        deviceReference = database.getReference("devices")
//
//        // Get the device's unique ID
//        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//
//        // Check if device details exist in Firebase
//        lifecycleScope.launch {
//            delay(2000) // 2 seconds for splash screen display
//            checkDeviceDetails()
//        }
//    }
//
//    private fun checkDeviceDetails() {
//        deviceReference.child(androidId).get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val snapshot = task.result
//                if (snapshot.exists()) {
//                    // If device details exist, navigate to HomeActivity
//                    navigateToHome()
//                } else {
//                    // If device details do not exist, navigate to GetStartedActivity
//                    navigateToGetStarted()
//                }
//            } else {
//                // Handle Firebase query error if needed
//                navigateToGetStarted()
//            }
//        }
//    }
//
//    private fun navigateToHome() {
//        val intent = Intent(this, HomeActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    private fun navigateToGetStarted() {
//        val intent = Intent(this, DeviceNameActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//}
package com.example.locationtracker.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        deviceReference = database.getReference("devices")

        // Get the device's unique ID
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        Log.d("SplashActivity", "Device Android ID: $androidId")

        // Check if device details exist in Firebase
        lifecycleScope.launch {
            delay(2000) // 2 seconds for splash screen display
            checkDeviceDetails()
        }
    }
    override fun onResume() {
        super.onResume()
        checkDeviceDetails()
    }

    private fun checkDeviceDetails() {
        Log.d("SplashActivity", "Checking device details for Android ID: $androidId")

        deviceReference.child(androidId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
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

}
