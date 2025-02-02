package com.example.locationtracker.screens

import Checker
import PermissionHelper
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.databinding.ActivitySplashBinding
import com.example.locationtracker.models.DeviceProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
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
        applySavedLanguage()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checker = Checker(this)

        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        if (checker.isInternetEnabled()) {
            fetchDeviceProfile(androidId)
        } else {
            showInternetDisabledDialog()
        }


    }

    private fun fetchDeviceProfile(deviceId: String) {
        // Log the initiation of the API call for debugging
        Log.d("DeviceProfile", "Fetching device profile for device ID: $deviceId")

        RetrofitClient.apiService.getDeviceProfile(deviceId).enqueue(object : Callback<DeviceProfileResponse> {
            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
                if (response.isSuccessful) {
                    val deviceProfileResponse = response.body()

                    if (deviceProfileResponse != null && deviceProfileResponse.success && deviceProfileResponse.profile != null) {
                        // If the device exists, log the success and navigate to HomeActivity
                        Log.d("DeviceProfile", "Device exists: ${deviceProfileResponse.profile}")
                        navigateToHome() // Navigate to Home screen
                    } else {
                        // Log the error and navigate to DeviceNameActivity
                        Log.e("DeviceProfile", "Device not found or profile is null. Response: $deviceProfileResponse")
                        navigateToGetStarted() // Navigate to DeviceName screen
                    }
                } else {
                    // Log API error codes for debugging
                    Log.e("DeviceProfile", "Error Code: ${response.code()}, Message: ${response.message()}")
                    navigateToGetStarted() // Fallback navigation to DeviceNameActivity
                }
            }

            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                // Log the failure details for debugging
                Log.e("DeviceProfile", "API call failed: ${t.message}")
                navigateToGetStarted() // Fallback navigation
            }
        })
    }


    private fun checkAndRequestPermissions() {
        if (!checker.isInternetEnabled()) {
            showInternetDisabledDialog()
            return
        }

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
                dialog?.dismiss()  // Ensure the dialog is dismissed if internet is enabled
            } else {
                // Show the dialog again if the internet is still disabled
                showInternetDisabledDialog()
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

    private fun applySavedLanguage() {
        val savedLanguageCode = getSavedLanguagePreference() ?: return
        val locale = Locale(savedLanguageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun getSavedLanguagePreference(): String? {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("LanguageCode", "en") // Default to English ("en")
    }


}
