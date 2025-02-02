package com.example.locationtracker.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        androidId = getAndroidId()
        fetchDeviceProfile(androidId)

        binding.btnDone.setOnClickListener {
            val updatedName = binding.displayNameEdit.text.toString()
//            updateProfile(updatedName, selectedImageUri)
        }

        binding.changeImageIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imageView11.setImageURI(selectedImageUri)
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun fetchDeviceProfile(deviceId: String) {
        RetrofitClient.apiService.getDeviceProfile(deviceId).enqueue(object : Callback<DeviceProfileResponse> {
            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()?.profile
                    profile?.let {
                        binding.displayNameDi.text = it.deviceName
                        binding.displayNameEdit.setText(it.deviceName)
                        Log.d("Profile Image ","${it.image}")
                        Glide.with(this@ProfileActivity).load(it.image).into(binding.imageView11)
//                        if (!it.image.isNullOrEmpty()) {
//                            Glide.with(this@ProfileActivity).load(it.image).into(binding.imageView11)
//                        }
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Error loading profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Failed to fetch profile", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun updateProfile(updatedName: String, imageUri: Uri?) {
//        val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), updatedName)
//        val imagePart = imageUri?.let { uri ->
//            val file = getFileFromUri(uri)
//            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//            MultipartBody.Part.createFormData("image", file.name, requestBody)
//        }
//
//        RetrofitClient.apiService.updateDeviceProfile(androidId, namePart, imagePart).enqueue(object : Callback<DeviceProfileResponse> {
//            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@ProfileActivity, "Profile updated!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@ProfileActivity, "Update failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
//                Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "profile_image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}
