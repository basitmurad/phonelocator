package com.example.locationtracker.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.databinding.ActivityProfileBinding
import com.example.locationtracker.models.DeviceProfileResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var androidId: String
    private var selectedImageUri: Uri? = null
    private var progressDialog: ProgressDialog? = null

    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.imageView11.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Updating profile...")
            setCancelable(false) // Prevent dismissal by back button
        }

        androidId = getAndroidId()
        fetchDeviceProfile(androidId)

        binding.btnDone.setOnClickListener {
            val updatedName = binding.displayNameEdit.text.toString()

            if (updatedName.isNotEmpty() || selectedImageUri != null) {
                updateDeviceProfile(updatedName, selectedImageUri)
            } else {
                Toast.makeText(this, "No changes to update", Toast.LENGTH_SHORT).show()
            }
        }

        binding.changeImageIcon.setOnClickListener {
            getImageResult.launch("image/*")
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun fetchDeviceProfile(deviceId: String) {
        progressDialog?.show()

        RetrofitClient.apiService.getDeviceProfile(deviceId).enqueue(object : Callback<DeviceProfileResponse> {
            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
                progressDialog?.dismiss()

                if (response.isSuccessful) {
                    val profile = response.body()?.profile
                    profile?.let {
                        binding.displayNameDi.text = it.deviceName
                        binding.displayNameEdit.setText(it.deviceName)
                        Glide.with(this@ProfileActivity).load(it.image).into(binding.imageView11)
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Error loading profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(this@ProfileActivity, "Failed to fetch profile", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateDeviceProfile(updatedName: String, selectedImageUri: Uri?) {
        val deviceId = getAndroidId()

        // Create request body for device name
        val deviceNameRequestBody = if (updatedName.isNotEmpty()) {
            updatedName.toRequestBody("text/plain".toMediaTypeOrNull())
        } else {
            null
        }

        // Convert image to MultipartBody.Part
        val imagePart = selectedImageUri?.let { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File(cacheDir, "profile_image.jpg")

            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", tempFile.name, requestFile)
        }

        progressDialog?.show()

        RetrofitClient.apiService.updateDevice(deviceId, deviceNameRequestBody, imagePart)
            .enqueue(object : Callback<DeviceProfileResponse> {
                override fun onResponse(
                    call: Call<DeviceProfileResponse>,
                    response: Response<DeviceProfileResponse>
                ) {
                    progressDialog?.dismiss()
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()


                        Log.e("ProfileUpdate", "Server Response: ${response.body()}")

                        fetchDeviceProfile(deviceId)  // Refresh profile
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ProfileUpdateError", "Server Response: $errorBody")
                        Toast.makeText(this@ProfileActivity, "Error updating profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                    progressDialog?.dismiss()
                    Log.e("ProfileUpdateError", "Network Failure: ${t.message}")
                    Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            })
    }


//    private fun updateDeviceProfile(updatedName: String, selectedImageUri: Uri?) {
//        val deviceId = getAndroidId()
//
//        // Create request body for device name
//        val deviceNameRequestBody: RequestBody? = if (updatedName.isNotEmpty()) {
//            RequestBody.create("text/plain".toMediaTypeOrNull(), updatedName)
//        } else {
//            null
//        }
//
//        // Convert image to binary format and create MultipartBody.Part
//        val imagePart: MultipartBody.Part? = selectedImageUri?.let { uri ->
//            val inputStream = contentResolver.openInputStream(uri)
//            val tempFile = File(cacheDir, "profile_image.jpg")
//            val outputStream = FileOutputStream(tempFile)
//            inputStream?.copyTo(outputStream)
//            outputStream.close()
//            inputStream?.close()
//
//            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), tempFile)
//            MultipartBody.Part.createFormData("image", tempFile.name, requestFile)
//        }
//
//        // Make the API call
//        RetrofitClient.apiService.updateDevice(deviceId, deviceNameRequestBody, imagePart).enqueue(object : Callback<DeviceProfileResponse> {
//            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
//                    fetchDeviceProfile(deviceId)
//                } else {
//                    Toast.makeText(this@ProfileActivity, "Error updating profile", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
//                Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

}
