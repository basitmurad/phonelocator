package com.example.locationtracker.screens

import Checker
import PermissionHelper
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.locationtracker.DeviceNameActivity
import com.example.locationtracker.HomeActivity
import com.example.locationtracker.R
import com.example.locationtracker.databinding.ActivitySplashBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var deviceReference: DatabaseReference
    private lateinit var androidId: String
    private lateinit var checker: Checker
    private lateinit var permissionHelper: PermissionHelper
    private var dialog: AlertDialog? = null  // Declare dialog here

    companion object {
        private const val REQUEST_CODE_INTERNET_SETTINGS = 1001
    }

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

        checkAndRequestPermissions()
    }

    override fun onResume() {
        super.onResume()
        // Check and request necessary permissions
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        if (!checker.isInternetEnabled()) {
            showInternetDisabledDialog()
            return
        }

        checkDeviceDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun showInternetDisabledDialog() {
        if (dialog != null && dialog!!.isShowing) {
            return  // Avoid showing the dialog if it's already shown
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_generic_disabled, null)

        dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set up dialog views
        dialogView.findViewById<ImageView>(R.id.dialog_icon)
            .setImageResource(R.drawable.logo2) // Replace with your icon
        dialogView.findViewById<TextView>(R.id.dialog_title).text =
            getString(R.string.internet_disabled)
        dialogView.findViewById<TextView>(R.id.dialog_message).text =
            getString(R.string.internet_connection_is_required_for_this_app_to_function_properly_please_enable_internet_connectivity)

        // Handle button clicks
        dialogView.findViewById<AppCompatButton>(R.id.btn_settings).setOnClickListener {
            startActivityForResult(
                Intent(Settings.ACTION_WIRELESS_SETTINGS),
                REQUEST_CODE_INTERNET_SETTINGS
            )
            dialog?.dismiss()  // Dismiss the dialog when the button is clicked
        }
        dialogView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
            dialog?.dismiss()
            finish() // Close the app if the user cancels
        }

        dialog?.setOnDismissListener {
            // Recheck permissions when the dialog is dismissed
            checkAndRequestPermissions()
        }

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.show()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_INTERNET_SETTINGS) {
            // Check if the internet is now enabled
            if (checker.isInternetEnabled()) {
                // Proceed with checking device details
                checkDeviceDetails()
                dialog?.dismiss()  // Ensure the dialog is dismissed if internet is enabled
            } else {
                // Show the dialog again if the internet is still disabled
                showInternetDisabledDialog()
            }
        }
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
}
