package com.example.locationtracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.models.AddDeviceRequest
import com.example.locationtracker.models.AddDeviceResponse
import com.example.locationtracker.models.DeviceProfileResponse


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val device = AddDeviceRequest(
            deviceId = "9347080e93f705da",  // This should ideally be dynamically assigned
            deviceName = "ali shan"
        )



        val connectionId = "9347080e93f705da"


//        fetchConnections("9347080e93f705da")
    }



    private fun registerDevice(device: AddDeviceRequest) {
        RetrofitClient.apiService.addDevice(device).enqueue(object : Callback<AddDeviceResponse> {
            override fun onResponse(call: Call<AddDeviceResponse>, response: Response<AddDeviceResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.success) {
                        Toast.makeText(this@MainActivity2, result.message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity2, "Failed to register device", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity2, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AddDeviceResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity2, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun fetchDeviceProfile(deviceId: String) {
        RetrofitClient.apiService.getDeviceProfile(deviceId).enqueue(object : Callback<DeviceProfileResponse> {
            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
                if (response.isSuccessful) {
                    val deviceProfileResponse = response.body()
                    if (deviceProfileResponse != null && deviceProfileResponse.success) {
                        val profile = deviceProfileResponse.profile
                        Log.d("DeviceProfile", "Success: ${deviceProfileResponse.success}")
                        Log.d("DeviceProfile", "Message: ${deviceProfileResponse.message}")
                        Log.d("DeviceProfile", "ID: ${profile._id}")
                        Log.d("DeviceProfile", "Device ID: ${profile.deviceId}")
                        Log.d("DeviceProfile", "Device Name: ${profile.deviceName}")
                        Log.d("DeviceProfile", "Created At: ${profile.createdAt}")
                        Log.d("DeviceProfile", "Updated At: ${profile.updatedAt}")
                        Log.d("DeviceProfile", "Version: ${profile.__v}")
                        Log.d("DeviceProfile", "image: ${profile.image}")
                    } else {
                        Log.e("DeviceProfile", "Failed: ${deviceProfileResponse?.message}")
                    }
                } else {
                    Log.e("DeviceProfile", "Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                Log.e("DeviceProfile", "Failure: ${t.message}")
            }
        })
    }
}


