package com.example.locationtracker
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.locationtracker.databinding.ActivityHomeBinding
import com.example.locationtracker.fragments.ConnectFragment
import com.example.locationtracker.fragments.DeviceFragment
import com.example.locationtracker.fragments.ProfileFragment
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        showGeneratedCode()
        // Load the default fragment (e.g., HomeFragment)
        loadFragment(HomeFragment())
    }

    override fun onResume() {
        super.onResume()

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

    @SuppressLint("BatteryLife")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndRequestBatteryOptimizationPermission() {
        if (!isIgnoringBatteryOptimizations()) {
            // If battery optimizations are not ignored, open the settings to request it
            val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = android.net.Uri.parse("package:${packageName}")
            startActivityForResult(intent, REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATIONS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

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

    @SuppressLint("BatteryLife")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showBatteryOptimizationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Battery Optimization Required")
            .setMessage("For this app to function properly, it needs to run in the background without battery restrictions. Please go to settings and enable 'Don't optimize' for this app.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = android.net.Uri.parse("package:${packageName}")
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
