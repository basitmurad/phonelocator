package com.example.locationtracker

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
import com.example.locationtracker.models.LocationHistoryResponse
import com.example.locationtracker.models.LocationRequest
import com.example.locationtracker.models.LocationResponse
import com.example.locationtracker.models.RecentLocationResponse
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


        val locationRequest = LocationRequest(
            deviceId = "9347080e93f705da",
            lat = "148.35",
            lng = "459.242",
            batteryPercentage = 60,
            date = "2025-02-03T00:00:00.000Z",
            time = "2:31:07 a.m."
        )
//        addLocation(context = this, locationRequest = locationRequest)

//        // Example: Get location history
        val connectionId = "9347080e93f705da"
        getLocationHistory(context = this, connectionId = connectionId)
//
//        // Example: Get recent locations
//        getRecentLocations(context = this, connectionId = connectionId)

//        registerDevice(device)
//        fetchDeviceProfile("90909090")
//
//        updateDeviceProfile("987985865","samsung1212","1737876915867_alex-mccarthy-RGKdWJOUFH0-unsplash.jpg")
//
//        fetchDeviceProfile("9347080e93f705da")

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
    fun addLocation(context: Context, locationRequest: LocationRequest) {
        RetrofitClient.apiService.addLocation(locationRequest).enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Location added successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to add location", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getLocationHistory(context: Context, connectionId: String) {
        RetrofitClient.apiService.getLocationHistory(connectionId).enqueue(object : Callback<LocationHistoryResponse> {
            override fun onResponse(call: Call<LocationHistoryResponse>, response: Response<LocationHistoryResponse>) {
                if (response.isSuccessful) {
                    val locationHistory = response.body()?.locations
                        Log.d("location","$locationHistory")
                    // Handle the location history response (e.g., display in a RecyclerView or log the result)
                    Toast.makeText(context, "Location history fetched!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to fetch location history", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LocationHistoryResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getRecentLocations(context: Context, connectionId: String) {
        RetrofitClient.apiService.getRecentLocations(connectionId).enqueue(object : Callback<RecentLocationResponse> {
            override fun onResponse(call: Call<RecentLocationResponse>, response: Response<RecentLocationResponse>) {
                if (response.isSuccessful) {
                    val recentLocations = response.body()?.locations
                    // Handle the recent locations response (e.g., display in a RecyclerView or log the result)
                    Toast.makeText(context, "Recent locations fetched!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to fetch recent locations", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RecentLocationResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }



//    private fun updateDeviceProfile(deviceId: String, deviceName: String?, image: String?) {
//        // Create the update request with the new device name and image
//        val updateRequest = UpdateDeviceRequest(deviceName, image)
//
//        // Make the API call to update the profile
//        RetrofitClient.apiService.updateDeviceProfile(deviceId, updateRequest).enqueue(object : Callback<DeviceProfileResponse> {
//            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
//                if (response.isSuccessful) {
//                    // Log the success message from the response body
//                    val message = response.body()?.message ?: "Update successful"
//                    Log.d("UpdateDeviceProfile", "Update successful: $message")
//
//                    // You can also handle further logic, like fetching the updated profile here if needed
//                } else {
//                    // Log the error details if the response is not successful
//                    Log.e("UpdateDeviceProfile", "Error: ${response.code()} - ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
//                // Log failure message
//                Log.e("UpdateDeviceProfile", "Failure: ${t.message}")
//            }
//        })
//    }

//    private fun updateDeviceProfile(deviceId: String, deviceName: String?, image: String?) {
//        val updateRequest = UpdateDeviceRequest(deviceName, image)
//
//        RetrofitClient.apiService.updateDeviceProfile(deviceId, updateRequest).enqueue(object : Callback<DeviceProfileResponse> {
//            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
//                if (response.isSuccessful) {
//                    val rawResponse = response.body()
//                    Log.d("UpdateDeviceProfile", "Raw response body: $rawResponse")
//
//                    // Log success message or any other useful information
//                    Log.d("UpdateDeviceProfile", "Response message: ${response.message()}")
//
//                    // If profile is null, we might only need the success message
//                    val successMessage = "Update successful, no body returned"
//                    Log.d("UpdateDeviceProfile", successMessage)
//                } else {
//                    // Log error code and error body (if available)
//                    Log.e("UpdateDeviceProfile", "Error Code: ${response.code()} - ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
//                Log.e("UpdateDeviceProfile", "Failure: ${t.message}")
//            }
//        })
//    }
}
