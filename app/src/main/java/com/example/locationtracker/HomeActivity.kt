//package com.example.locationtracker
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.fragment.app.Fragment
//import com.example.locationtracker.databinding.ActivityHomeBinding
//import com.example.locationtracker.fragments.ConnectFragment
//import com.example.locationtracker.fragments.DeviceFragment
//import com.example.locationtracker.fragments.ProfileFragment
//import kotlin.random.Random
//
//class HomeActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityHomeBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//        // Set up BottomNavigationView listener
//        binding.bottomNavigationView.itemRippleColor = null
//
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_home -> {
//                    loadFragment(HomeFragment())
//                    true
//                }
//                R.id.nav_profile -> {
//                    loadFragment(ProfileFragment())
//                    true
//                }
//                R.id.nav_devices -> {
//                    loadFragment(DeviceFragment())
//                    true
//                }
//                R.id.nav_connect -> {
//                    loadFragment(ConnectFragment())
//                    true
//                }
//                else -> false
//            }
//        }
//
//        showGeneratedCode()
//        // Load the default fragment (e.g., HomeFragment)
//        loadFragment(HomeFragment())
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        // Check and request battery optimization exclusion every time the activity resumes
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkAndRequestBatteryOptimizationPermission()
//        }
//    }
//
//    private fun loadFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(binding.fragmentContainer.id, fragment) // Use the ID of the FrameLayout
//            .commit()
//    }
//
//    @SuppressLint("BatteryLife")
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun checkAndRequestBatteryOptimizationPermission() {
//        if (!isIgnoringBatteryOptimizations()) {
//            // If battery optimizations are not ignored, open the settings to request it
//            val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
//            intent.data = android.net.Uri.parse("package:${packageName}")
//            startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS)
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun isIgnoringBatteryOptimizations(): Boolean {
//        val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
//        return powerManager.isIgnoringBatteryOptimizations(packageName)
//    }
//
//    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS) {
//            if (isIgnoringBatteryOptimizations()) {
//                // Permission granted, proceed normally
//                // You can display a success message if needed
//            } else {
//                // Permission denied or not set yet, show a dialog to explain
//                showBatteryOptimizationDialog()
//            }
//        }
//    }
//
//    @SuppressLint("BatteryLife")
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun showBatteryOptimizationDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("Battery Optimization Required")
//            .setMessage("For this app to function properly, it needs to run in the background without battery restrictions. Please go to settings and enable 'Don't optimize' for this app.")
//            .setPositiveButton("Go to Settings") { _, _ ->
//                val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
//                intent.data = android.net.Uri.parse("package:${packageName}")
//                startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS)
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .show()
//    }
//
//    companion object {
//        private const val REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS = 1
//    }
//
//    private fun generateUniqueCode(length: Int): String {
//        val characters = "abcdefghijklmnopqrstuvwxyz0123456789"
//        val random = Random
//        val stringBuilder = StringBuilder()
//
//        for (i in 0 until length) {
//            val randomIndex = random.nextInt(characters.length)
//            stringBuilder.append(characters[randomIndex])
//        }
//
//        return stringBuilder.toString()
//    }
//
//    // Example usage of generating a 6-digit unique code
//    private fun showGeneratedCode() {
//        val uniqueCode = generateUniqueCode(6)
//        println("Generated Unique Code: $uniqueCode")
//    }
//
//}
package com.example.locationtracker

import Checker
import PermissionHelper
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.locationtracker.databinding.ActivityHomeBinding
import com.example.locationtracker.fragments.ConnectFragment
import com.example.locationtracker.fragments.DeviceFragment
import com.example.locationtracker.fragments.ProfileFragment
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var checker: Checker
    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        checker = Checker(this)
        permissionHelper = PermissionHelper(this)

        // Set up BottomNavigationView listener
        binding.bottomNavigationView.itemRippleColor = null

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                R.id.nav_devices -> {
                    loadFragment(DeviceFragment())
                    true
                }

                R.id.nav_connect -> {
                    loadFragment(ConnectFragment())
                    true
                }

                else -> false
            }
        }

        // Check and request necessary permissions
        checkAndRequestPermissions()

        showGeneratedCode()
        // Load the default fragment (e.g., HomeFragment)
        loadFragment(HomeFragment())
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onResume() {
        super.onResume()

        checkAndRequestPermissions()
        if (!checker.isInternetEnabled()) {
            showInternetDisabledDialog()
        }
        // Check and request battery optimization exclusion every time the activity resumes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestBatteryOptimizationPermission()
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment) // Use the ID of the FrameLayout
            .commit()
    }

    private fun checkAndRequestPermissions() {
        if (!checker.isInternetEnabled()) {
            showInternetDisabledDialog()
        }

        if (!checker.isLocationEnabled()) {
            // Show dialog or toast to enable location
        }

        if (!checker.isBatteryOptimizationUnrestricted()) {
            // Request to disable battery optimization
            checker.requestDisableBatteryOptimization()
        }

        if (!permissionHelper.checkLocationPermissions()) {
            permissionHelper.requestLocationPermissions()
        }

        if (!permissionHelper.checkBackgroundLocationPermission()) {
            permissionHelper.requestBackgroundLocationPermission()
        }
    }

    private fun showInternetDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Internet Disabled")
            .setMessage("Internet connection is required for this app to function properly. Please enable internet connectivity.")
            .setPositiveButton("Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @SuppressLint("BatteryLife", "ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndRequestBatteryOptimizationPermission() {
        if (!isIgnoringBatteryOptimizations()) {
            // If battery optimizations are not ignored, open the settings to request it
            val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = android.net.Uri.parse("package:$packageName")
            startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

    @SuppressLint("ObsoleteSdkInt")
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS) {
            if (isIgnoringBatteryOptimizations()) {
                // Permission granted, proceed normally
                // You can display a success message if needed
            } else {
                // Permission denied or not set yet, show a dialog to explain
                showBatteryOptimizationDialog()
            }
        }
    }

    @SuppressLint("BatteryLife", "ObsoleteSdkInt")
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

    companion object {
        private const val REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS = 1
    }

    private fun generateUniqueCode(length: Int): String {
        val characters = "abcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random
        val stringBuilder = StringBuilder()

        for (i in 0 until length) {
            val randomIndex = random.nextInt(characters.length)
            stringBuilder.append(characters[randomIndex])
        }

        return stringBuilder.toString()
    }

    // Example usage of generating a 6-digit unique code
    private fun showGeneratedCode() {
        val uniqueCode = generateUniqueCode(6)
        println("Generated Unique Code: $uniqueCode")
    }

}


//class HomeActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var checker: Checker
//    private lateinit var permissionHelper: PermissionHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//        checker = Checker(this)
//        permissionHelper = PermissionHelper(this)
//
//        // Set up BottomNavigationView listener
//        binding.bottomNavigationView.itemRippleColor = null
//
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_home -> {
//                    loadFragment(HomeFragment())
//                    true
//                }
//
//                R.id.nav_profile -> {
//                    loadFragment(ProfileFragment())
//                    true
//                }
//
//                R.id.nav_devices -> {
//                    loadFragment(DeviceFragment())
//                    true
//                }
//
//                R.id.nav_connect -> {
//                    loadFragment(ConnectFragment())
//                    true
//                }
//
//                else -> false
//            }
//        }
//
//        // Check and request necessary permissions
//        checkAndRequestPermissions()
//
//        showGeneratedCode()
//        // Load the default fragment (e.g., HomeFragment)
//        loadFragment(HomeFragment())
//    }
//
//    @SuppressLint("ObsoleteSdkInt")
//    override fun onResume() {
//        super.onResume()
//
//        // Check and request battery optimization exclusion every time the activity resumes
//       checkAndRequestPermissions()
//    }
//
//    private fun loadFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(binding.fragmentContainer.id, fragment) // Use the ID of the FrameLayout
//            .commit()
//    }
//
//    private fun checkAndRequestPermissions() {
//        // Check if the internet is enabled, if not, show a dialog
//        if (!checker.isInternetEnabled()) {
//            showInternetDialog()
//        }
//
//        // Check if location is enabled, if not, show a dialog
//        if (!checker.isLocationEnabled()) {
//            showLocationDialog()
//        }
//        // Check if battery optimization is unrestricted
//
//        if (!checker.isBatteryOptimizationUnrestricted()) {
//            // Request to disable battery optimization
//            checker.requestDisableBatteryOptimization()
//        }
//
//        // Request location permissions if needed
//        if (!permissionHelper.checkLocationPermissions()) {
//            permissionHelper.requestLocationPermissions()
//        }
//
//        // Request background location permission if needed
//        if (!permissionHelper.checkBackgroundLocationPermission()) {
//            permissionHelper.requestBackgroundLocationPermission()
//        }
//    }
//
//    private fun showInternetDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("Internet Required")
//            .setMessage("Internet connection is required for this app to function. Please enable your internet connection.")
//            .setPositiveButton("OK") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .setCancelable(false)
//            .show()
//    }
//
//    private fun showLocationDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("Location Required")
//            .setMessage("Location services are required for this app to function. Please enable location in your settings.")
//            .setPositiveButton("OK") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .setCancelable(false)
//            .show()
//    }
//
//    @SuppressLint("BatteryLife", "ObsoleteSdkInt")
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun checkAndRequestBatteryOptimizationPermission() {
//        if (!isIgnoringBatteryOptimizations()) {
//            // If battery optimizations are not ignored, open the settings to request it
//            val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
//            intent.data = android.net.Uri.parse("package:$packageName")
//            startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS)
//        }
//    }
//
//    @SuppressLint("ObsoleteSdkInt")
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun isIgnoringBatteryOptimizations(): Boolean {
//        val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
//        return powerManager.isIgnoringBatteryOptimizations(packageName)
//    }
//
//    @SuppressLint("ObsoleteSdkInt")
//    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS) {
//            if (isIgnoringBatteryOptimizations()) {
//                // Permission granted, proceed normally
//                // You can display a success message if needed
//            } else {
//                // Permission denied or not set yet, show a dialog to explain
//                showBatteryOptimizationDialog()
//            }
//        }
//    }
//
//    @SuppressLint("BatteryLife", "ObsoleteSdkInt")
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun showBatteryOptimizationDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("Battery Optimization Required")
//            .setMessage("For this app to function properly, it needs to run in the background without battery restrictions. Please go to settings and enable 'Don't optimize' for this app.")
//            .setPositiveButton("Go to Settings") { _, _ ->
//                val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
//                intent.data = android.net.Uri.parse("package:$packageName")
//                startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS)
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .show()
//    }
//
//    companion object {
//        private const val REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS = 1
//    }
//
//
//    @SuppressLint("HardwareIds")
//    fun generateDeviceCode(context: Context, length: Int): String {
//        // Get the device ID (ANDROID_ID)
//        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
//
//        // Define characters for the random part of the code
//        val characters = "abcdefghijklmnopqrstuvwxyz0123456789"
//        val random = Random
//        val stringBuilder = StringBuilder()
//
//        // Generate a random code
//        for (i in 0 until length) {
//            val randomIndex = random.nextInt(characters.length)
//            stringBuilder.append(characters[randomIndex])
//        }
//
//        // Combine the device ID with the random code
//        return deviceId + "-" + stringBuilder.toString()
//    }
//
//    // Example usage of generating a 6-digit unique code
//    private fun showGeneratedCode() {
//        val uniqueCode = generateDeviceCode(6)
//        println("Generated Unique Code: $uniqueCode")
//    }
//
//}
