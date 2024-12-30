package com.example.locationtracker

import Checker
import HomeFragment
import PermissionHelper
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.locationtracker.databinding.ActivityHomeBinding
import com.example.locationtracker.fragments.ConnectFragment
import com.example.locationtracker.fragments.DeviceFragment
import com.example.locationtracker.fragments.ProfileFragment
import java.security.MessageDigest
import kotlin.random.Random

class HomeActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var checker: Checker
    private lateinit var permissionHelper: PermissionHelper
    private var internetDialog: AlertDialog? = null
    private lateinit var connectivityReceiver: ConnectivityReceiver

    companion object {
        private const val REQUEST_CODE_INTERNET_SETTINGS = 1001
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        checker = Checker(this)
        permissionHelper = PermissionHelper(this)
        connectivityReceiver = ConnectivityReceiver(this)

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

        // Get the device ID and generate a unique code from it
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val uniqueCode = generateCodeFromDeviceId(deviceId)
        Log.d("HomeActivity", "Generated Unique Code: $uniqueCode")


        showGeneratedCode()
        // Load the default fragment (e.g., HomeFragment)
        loadFragment(HomeFragment())
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        checkAndRequestPermissions()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityReceiver)
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

        if (!permissionHelper.checkLocationPermissions()) {
            permissionHelper.requestLocationPermissions()
        }

        if (!permissionHelper.checkBackgroundLocationPermission()) {
            permissionHelper.requestBackgroundLocationPermission()
        }
    }

    private fun showInternetDisabledDialog() {
        if (internetDialog?.isShowing == true) {
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_generic_disabled, null)

        internetDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set up dialog views
        dialogView.findViewById<ImageView>(R.id.dialog_icon).setImageResource(R.drawable.logo2) // Replace with your icon
        dialogView.findViewById<TextView>(R.id.dialog_title).text = getString(R.string.internet_disabled)
        dialogView.findViewById<TextView>(R.id.dialog_message).text = getString(R.string.internet_connection_is_required_for_this_app_to_function_properly_please_enable_internet_connectivity)

        // Handle button clicks
        dialogView.findViewById<AppCompatButton>(R.id.btn_settings).setOnClickListener {
            startActivityForResult(Intent(Settings.ACTION_WIRELESS_SETTINGS), REQUEST_CODE_INTERNET_SETTINGS)
            internetDialog?.dismiss()
        }
        dialogView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
            internetDialog?.dismiss()
        }

        internetDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        internetDialog?.show()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            showInternetDisabledDialog()
        } else {
            internetDialog?.dismiss()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_INTERNET_SETTINGS) {
            // Check if the internet is now enabled
            if (checker.isInternetEnabled()) {
                // Proceed with any actions needed when the internet is enabled
                // For example: load some data, refresh UI, etc.
                internetDialog?.dismiss()
            } else {
                // Show the dialog again if the internet is still disabled
                showInternetDisabledDialog()
            }
        }
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

    private fun generateCodeFromDeviceId(deviceId: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(deviceId.toByteArray())
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }

    // Example usage of generating a 6-digit unique code
    private fun showGeneratedCode() {
        val uniqueCode = generateUniqueCode(6)
        println("Generated Unique Code: $uniqueCode")
    }
}
