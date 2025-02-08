package com.example.locationtracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.api.ApiService
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.api.RetrofitClient.apiService
import com.example.locationtracker.models.AddDeviceRequest
import com.example.locationtracker.models.AddDeviceResponse
import com.example.locationtracker.models.ConnectionDetails
import com.example.locationtracker.models.ConnectionResponse
import com.example.locationtracker.models.DeviceProfileResponse
import com.example.locationtracker.models.LocationHistoryResponse
import com.example.locationtracker.models.LocationRequest
import com.example.locationtracker.models.LocationResponse


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

//        fetchConnections("66666")

//        fetchConnections("b29db941246f0f51")

        fetchConnections("9347080e93f705da")


        val newLocation = LocationRequest(
            deviceId = "66666",
            lat = "148.35",
            lng = "459.242",
            batteryPercentage = 60,
            date = "2025-02-03T00:00:00.000Z",
            time = "2:31:07 a.m."
        )



        addLocation(newLocation)

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

    fun fetchConnections(deviceId: String) {
        apiService.getConnections(deviceId).enqueue(object : Callback<List<ConnectionDetails>> {
            override fun onResponse(
                call: Call<List<ConnectionDetails>>,
                response: Response<List<ConnectionDetails>>
            ) {
                if (response.isSuccessful) {
                    val connections = response.body()
                    if (!connections.isNullOrEmpty()) {
                        for (connection in connections) {
                            Log.d("API_RESPONSE", "Device ID: ${connection}")
//                            Log.d("API_RESPONSE", "Connected Device ID: ${connection.connectionId._id}")
//                            Log.d("API_RESPONSE", "Connected Device Name: ${connection.connectionId.deviceName}")
                        }
                    } else {
                        Log.e("API_RESPONSE", "No connections found for this device.")
                    }
                } else {
                    Log.e("API_RESPONSE", "HTTP Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ConnectionDetails>>, t: Throwable) {
                Log.e("API_RESPONSE", "Network Error: ${t.message}")
            }
        })
    }


    fun addLocation(locationRequest: LocationRequest) {
        RetrofitClient.apiService.addLocation(locationRequest).enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
                if (response.isSuccessful) {
                    Log.d("Location", "Success: ${response.body()?.message}")
                } else {
                    Log.e("Location", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                Log.e("Location", "Failure: ${t.message}")
            }
        })
    }

    // Get Location History
    fun getLocationHistory(connectionId: String) {
        RetrofitClient.apiService.getLocationHistory(connectionId).enqueue(object : Callback<LocationHistoryResponse> {
            override fun onResponse(call: Call<LocationHistoryResponse>, response: Response<LocationHistoryResponse>) {
                if (response.isSuccessful) {
                    Log.d("Location History", "Received: ${response.body()?.locations}")
                } else {
                    Log.e("Location History", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LocationHistoryResponse>, t: Throwable) {
                Log.e("Location History", "Failure: ${t.message}")
            }
        })
    }

    // Get Recent Locations
    fun getRecentLocations(connectionId: String) {
        RetrofitClient.apiService.getRecentLocations(connectionId).enqueue(object : Callback<LocationHistoryResponse> {
            override fun onResponse(call: Call<LocationHistoryResponse>, response: Response<LocationHistoryResponse>) {
                if (response.isSuccessful) {
                    Log.d("Recent Locations", "Received: ${response.body()?.locations}")
                } else {
                    Log.e("Recent Locations", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LocationHistoryResponse>, t: Throwable) {
                Log.e("Recent Locations", "Failure: ${t.message}")
            }
        })
    }


}


