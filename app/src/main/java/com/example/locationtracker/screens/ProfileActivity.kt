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

        val deviceNameRequestBody: RequestBody? = if (updatedName.isNotEmpty()) {
            RequestBody.create("text/plain".toMediaTypeOrNull(), updatedName)
        } else {
            null
        }

        val imagePart: MultipartBody.Part? = if (selectedImageUri != null) {
            val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri)
            val imageBytes = inputStream?.readBytes()

            if (imageBytes != null) {
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)
                MultipartBody.Part.createFormData("image", "profile_image.jpg", requestFile)
            } else {
                null
            }
        } else {
            null
        }

        RetrofitClient.apiService.updateDevice(deviceId, deviceNameRequestBody, imagePart).enqueue(object : Callback<DeviceProfileResponse> {
            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    fetchDeviceProfile(deviceId)
                } else {
                    Toast.makeText(this@ProfileActivity, "Error updating profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
