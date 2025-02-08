//package com.example.locationtracker.fragments
//
//import android.annotation.SuppressLint
//import android.app.AlertDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import com.example.locationtracker.R
//import com.example.locationtracker.api.RetrofitClient
//import com.example.locationtracker.models.DeviceProfileResponse
//import com.example.locationtracker.screens.LanguageActivity
//import com.example.locationtracker.screens.PrivacyPolicyActivity
//import com.example.locationtracker.screens.ProfileActivity
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//
//class ProfileFragment : Fragment() {
//
//    private lateinit var buttonOpen: LinearLayout
//    private lateinit var buttonRateUs: LinearLayout
//    private lateinit var btnLanguage: LinearLayout
//    private lateinit var btnFeedback: LinearLayout
//    private lateinit var btnShare: LinearLayout
//    private lateinit var name: TextView
//
//    private lateinit var androidId: String
//    private lateinit var deviceName: String
//    private lateinit var textname: TextView
//    private lateinit var profileImage: ImageView
//    private lateinit var btnClickSHow: ImageView
//
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//
//        buttonOpen = view.findViewById(R.id.btnPrivacy)
//        buttonRateUs = view.findViewById(R.id.btnRateUs)
//        name = view.findViewById(R.id.profileName)
//        btnShare = view.findViewById(R.id.btnShareApp)
//        textname = view.findViewById(R.id.textname)
//        btnFeedback = view.findViewById(R.id.btnFeedback)
//        btnLanguage = view.findViewById(R.id.btnLanguage)
//        profileImage = view.findViewById(R.id.imageview)
//        btnClickSHow = view.findViewById(R.id.btnClickSHow)
//
//        androidId = getAndroidId()
//
//
//        fetchDeviceProfile(androidId)
//
//
//        btnFeedback.setOnClickListener {
//            goNextScreen()
//        }
//
//        btnLanguage.setOnClickListener {
//            val intent = Intent(requireContext(), LanguageActivity::class.java)
//            startActivity(intent)
//        }
//
//        btnShare.setOnClickListener {
//            val apkFile = File(requireContext().filesDir, "Location Tracker.apk")
//
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "application/vnd.android.package-archive"
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile))
//
//            startActivity(Intent.createChooser(intent, "Share app via"))
//        }
//
//        btnClickSHow.setOnClickListener {
//            val intent = Intent(requireContext(), ProfileActivity::class.java)
//            val nameValue = name.text.toString() // Assuming name is a TextView
//            intent.putExtra("name", nameValue)
//            startActivity(intent)
//        }
//
//        buttonRateUs.setOnClickListener {
//            showRatingDialog()
//        }
//
//        return view
//    }
//
//    private fun goNextScreen() {
//        val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun showRatingDialog() {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rating, null)
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setCancelable(true)
//
//        val alertDialog = dialogBuilder.create()
//        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        alertDialog.show()
//    }
//
//
//
//
//    @SuppressLint("HardwareIds")
//    private fun getAndroidId(): String {
//        return requireContext().contentResolver.let {
//            android.provider.Settings.Secure.getString(it, android.provider.Settings.Secure.ANDROID_ID)
//        }
//    }
//
//    private fun fetchDeviceProfile(deviceId: String) {
//        RetrofitClient.apiService.getDeviceProfile(deviceId).enqueue(object : Callback<DeviceProfileResponse> {
//            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
//                if (response.isSuccessful) {
//                    val deviceProfileResponse = response.body()
//                    if (deviceProfileResponse != null && deviceProfileResponse.success) {
//                        val profile = deviceProfileResponse.profile
//                        Log.d("DeviceProfile", "Success: ${deviceProfileResponse.success}")
//                        Log.d("DeviceProfile", "Device Name: ${profile.deviceName}")
//                        Log.d("DeviceProfile", "imageUrl: ${profile.image}")
//
//                        // Set the device name and display the first character in a circle
//                        deviceName = profile.deviceName
//                        textname.text = deviceName
//
//
//                        // Set the first character in the circular profile image
//                        val firstChar = deviceName[0].toString()
//                        profileImage.setImageResource(R.drawable.circle_background) // Ensure this is a circular background image in drawable
//                        textname.text = firstChar
//                        name.text = deviceName
//                    } else {
//                        Log.e("DeviceProfile", "Failed: ${deviceProfileResponse?.message}")
//                    }
//                } else {
//                    Log.e("DeviceProfile", "Error Code: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
//                Log.e("DeviceProfile", "Failure: ${t.message}")
//            }
//        })
//    }
//
//
//
//}
//

package com.example.locationtracker.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.locationtracker.R
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.models.DeviceProfileResponse
import com.example.locationtracker.screens.LanguageActivity
import com.example.locationtracker.screens.PrivacyPolicyActivity
import com.example.locationtracker.screens.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var buttonOpen: LinearLayout
    private lateinit var buttonRateUs: LinearLayout
    private lateinit var btnLanguage: LinearLayout
    private lateinit var btnFeedback: LinearLayout
    private lateinit var btnShare: LinearLayout
    private lateinit var name: TextView
    private lateinit var textname: TextView
    private lateinit var profileImage: ImageView
    private lateinit var btnClickShow: ImageView

    private lateinit var androidId: String
    private lateinit var deviceName: String

    companion object {
        private const val REQUEST_PERMISSION_CODE = 100
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        buttonOpen = view.findViewById(R.id.btnPrivacy)
        buttonRateUs = view.findViewById(R.id.btnRateUs)
        name = view.findViewById(R.id.profileName)
        btnShare = view.findViewById(R.id.btnShareApp)
        textname = view.findViewById(R.id.textname)
        btnFeedback = view.findViewById(R.id.btnFeedback)
        btnLanguage = view.findViewById(R.id.btnLanguage)
        profileImage = view.findViewById(R.id.imageview)
        btnClickShow = view.findViewById(R.id.btnClickSHow)

        androidId = getAndroidId()

        // Check and request permission if needed (Android 10 and below)
        checkAndRequestPermissions()

        // Fetch device profile
        fetchDeviceProfile(androidId)

        btnFeedback.setOnClickListener { goNextScreen() }

        btnLanguage.setOnClickListener {
            val intent = Intent(requireContext(), LanguageActivity::class.java)
            startActivity(intent)
        }

        btnShare.setOnClickListener {
            val apkFile = File(requireContext().filesDir, "Location Tracker.apk")
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/vnd.android.package-archive"
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile))
            startActivity(Intent.createChooser(intent, "Share app via"))
        }

        btnClickShow.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            val nameValue = name.text.toString()
            intent.putExtra("name", nameValue)
            startActivity(intent)
        }

        buttonRateUs.setOnClickListener { showRatingDialog() }

        return view
    }

    private fun goNextScreen() {
        val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
        startActivity(intent)
    }

    private fun showRatingDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rating, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return requireContext().contentResolver.let {
            Settings.Secure.getString(it, Settings.Secure.ANDROID_ID)
        }
    }

    private fun fetchDeviceProfile(deviceId: String) {
        RetrofitClient.apiService.getDeviceProfile(deviceId).enqueue(object : Callback<DeviceProfileResponse> {
            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
                if (response.isSuccessful) {
                    val deviceProfileResponse = response.body()
                    if (deviceProfileResponse != null && deviceProfileResponse.success) {
                        val profile = deviceProfileResponse.profile
                        Log.d("DeviceProfile", "Success: ${deviceProfileResponse.success}")
                        Log.d("DeviceProfile", "Device Name: ${profile.deviceName}")
                        Log.d("DeviceProfile", "Image URL: ${profile.image}")

                        deviceName = profile.deviceName
                        name.text = deviceName

                        if (profile.image.isNotEmpty()) {
                            // Load image using Glide if URL exists
                            Glide.with(this@ProfileFragment)
                                .load(profile.image)
                                .placeholder(R.drawable.circle_background)
                                .into(profileImage)

                            // Hide text view since image exists
                            textname.visibility = View.GONE
                        } else {
                            // Set the first character if image URL is missing
                            val firstChar = deviceName[0].toString().uppercase()
                            textname.text = firstChar
                            textname.visibility = View.VISIBLE

                            // Set placeholder image
                            profileImage.setImageResource(R.drawable.circle_background)
                        }
                    } else {
                        Log.e("DeviceProfile", "Failed: ${deviceProfileResponse?.message}")
                        setDefaultProfile()
                    }
                } else {
                    Log.e("DeviceProfile", "Error Code: ${response.code()}")
                    setDefaultProfile()
                }
            }

            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                Log.e("DeviceProfile", "Failure: ${t.message}")
                setDefaultProfile()
            }
        })
    }

    private fun setDefaultProfile() {
        // If the API fails, set a default name and placeholder image
        name.text = "Unknown"
        textname.text = "U"
        textname.visibility = View.VISIBLE
        profileImage.setImageResource(R.drawable.circle_background)
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
            }
        }
    }
}
