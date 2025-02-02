package com.example.locationtracker

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.databinding.ActivityDeviceNameBinding
import com.example.locationtracker.models.AddDeviceRequest
import com.example.locationtracker.models.AddDeviceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceNameBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var model: String
    private lateinit var deviceName: String
    private lateinit var androidId: String
    private lateinit var manufacturer: String

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeviceNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this).apply {
            setTitle("Updating Device Details")
            setMessage("Please wait...")
            setCancelable(false)
        }

        // Fetch device information
        manufacturer = Build.MANUFACTURER ?: "Unknown"
        model = Build.MODEL ?: "Unknown"
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) ?: "Unknown"
        deviceName = "$manufacturer $model" // Combine manufacturer and model for device name

        // Set device name in the TextView
        binding.textView.text = Editable.Factory.getInstance().newEditable(deviceName)

        // Set click listener for the button
        binding.appCompatButton.setOnClickListener {
            if (deviceName.isNotEmpty()) {
                val deviceRequest = AddDeviceRequest(
                    deviceId= androidId,
                    deviceName = deviceName
                )
                registerDevice(deviceRequest)
            } else {
                Toast.makeText(this, "Device name is empty. Please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerDevice(device: AddDeviceRequest) {
        progressDialog.show()

        RetrofitClient.apiService.addDevice(device).enqueue(object : Callback<AddDeviceResponse> {
            override fun onResponse(call: Call<AddDeviceResponse>, response: Response<AddDeviceResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.success) {
                        Toast.makeText(this@DeviceNameActivity, result.message, Toast.LENGTH_LONG).show()
                        // Navigate to PermissionActivity
                        val intent = Intent(this@DeviceNameActivity, PermissionActivity::class.java)
                        startActivity(intent)
                        finish() // Close the current activity
                    } else {
                        Toast.makeText(this@DeviceNameActivity, "Failed to register device", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@DeviceNameActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AddDeviceResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DeviceNameActivity, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
