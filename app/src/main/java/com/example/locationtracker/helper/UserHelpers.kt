package com.example.locationtracker.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserHelpers(private val context: Context) {


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss") // Format for time
        return current.format(timeFormatter) ?: "Unknown Time"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Format for date
        return current.format(dateFormatter) ?: "Unknown Date"
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return try {
            context.contentResolver.let {
                android.provider.Settings.Secure.getString(it, android.provider.Settings.Secure.ANDROID_ID) ?: "Unknown Android ID"
            }
        } catch (e: Exception) {
            "Unknown Android ID"
        }
    }




}



//package com.example.locationtracker.fragments
//
//import Checker
//import HomeFragment
//import PermissionHelper
//import UserData
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.AlertDialog
//import android.app.ProgressDialog
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//
//import android.os.Bundle
//import android.os.Looper
//import android.provider.Settings
//import android.util.Log
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.locationtracker.R
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//import android.text.InputFilter
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.annotation.RequiresApi
//import androidx.appcompat.widget.AppCompatButton
//import androidx.core.app.ActivityCompat
//import com.example.locationtracker.helper.UserHelpers
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.util.logging.Handler
//
//class ConnectFragment : Fragment() {
//
//    private lateinit var appCompatButton2: Button
//    private lateinit var editText: EditText
//    private lateinit var progressDialog: ProgressDialog
//    private lateinit var permissionChecker: Checker
//    private lateinit var batteryPercentage: String
//    private lateinit var currentLatLng: LatLng
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var userHelpers: UserHelpers
//    private lateinit var enterCode:String
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_connect, container, false)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//
//        userHelpers = UserHelpers(requireContext())
//
//
//        progressDialog = ProgressDialog(requireContext())
//        progressDialog.setMessage("Please wait, locating your device")
//        progressDialog.setCancelable(false)
//
//        permissionChecker = Checker(requireContext())
//
//        val checker = Checker(requireContext())
//        batteryPercentage = checker.getBatteryPercentage().toString()
//        Toast.makeText(requireContext(), " your battter is$batteryPercentage", Toast.LENGTH_SHORT)
//            .show()
//        Log.d("Checker", "Battery Percentage: $batteryPercentage%")
//        appCompatButton2 = view.findViewById(R.id.appCompatButton2)
//        editText = view.findViewById(R.id.editText)
//
//        appCompatButton2.setOnClickListener{
//            progressDialog.show()
//            if (editText.text.isEmpty()){
//                progressDialog.dismiss()
//                Toast.makeText(requireContext(),"Please enter a code",Toast.LENGTH_SHORT).show()
//            }
//            else{
//
//                editText.filters = arrayOf(InputFilter.AllCaps())
//                enterCode = editText.text.toString().trim()
//
//                checkCodeInDevices(enterCode)
//
//            }
//        }
////        appCompatButton2.setOnClickListener {
////            progressDialog.show()
////            editText.filters = arrayOf(InputFilter.AllCaps())
////
////            val code = editText.text.toString().trim()
////
////
////            if (code.isEmpty()) {
////                progressDialog.dismiss()
////                Toast.makeText(requireContext(), "Please enter a code", Toast.LENGTH_SHORT).show()
////            } else {
////
////                // Check if the code matches any device
////                checkCodeInDevices(code)
////
////                // Fetch all devices and print their paths
////                val deviceChecker = DeviceChecker()
////                deviceChecker.fetchAllDevices { devicePaths ->
////                    progressDialog.dismiss()
////                    if (devicePaths.isNotEmpty()) {
////                        devicePaths.forEach { path ->
////                            println(path) // Log each device path
////                        }
//////                        Toast.makeText(requireContext(), "Device paths logged in console", Toast.LENGTH_SHORT).show()
////                    } else {
////                        Toast.makeText(requireContext(), "No devices found", Toast.LENGTH_SHORT)
////                            .show()
////                    }
////                }
////            }
////        }
//
//        return view
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @SuppressLint("MissingInflatedId")
//    private fun openDialog(deviceName: String ,matchDeviceID: String) {
//        val dialogView =
//            LayoutInflater.from(requireContext()).inflate(R.layout.confirm_dialog_layout, null)
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setCancelable(false)
//
//        val alertDialog = dialogBuilder.create()
//        val code: TextView = dialogView.findViewById(R.id.descriptionTextView122)
//        val btnNo: Button = dialogView.findViewById(R.id.button1)
//        val btnConfirm: Button = dialogView.findViewById(R.id.button2)
//
//
//
//
//
//        btnConfirm.setOnClickListener {
//
//            if (permissionChecker.isLocationEnabled()) {
//                Toast.makeText(
//                    requireContext().applicationContext,
//                    "Location is enabled",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                alertDialog.dismiss()
//
//                val currentUserDeviceId = userHelpers.getAndroidId();
//
//
//                createDeviceNode(deviceName ,currentUserDeviceId, matchDeviceID)
//                android.os.Handler(Looper.getMainLooper()).postDelayed({
//                    showSuccessDialog()
//                }, 500)
//
//            } else {
//                showLocationDisabledDialog()
//                Toast.makeText(
//                    requireContext().applicationContext,
//                    "Location is not enabled",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            }
//
//        }
//        btnNo.setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        code.text = deviceName
//
//        // Ensure the dialog background is transparent
//        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//        // Show the dialog first to get its window
//        alertDialog.show()
//
//        // Set dialog margins
//        val window = alertDialog.window
//        if (window != null) {
//            val layoutParams = window.attributes
//            val margin = 50 // Set margin in pixels (e.g., 50px)
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            window.setLayout(
//                (resources.displayMetrics.widthPixels - 2 * margin),
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            window.attributes = layoutParams
//        }
//    }
//
//
//    private fun showLocationDisabledDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_generic_disabled, null)
//
//        val locationDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .create()
//        locationDialog.setCancelable(false)
//
//        // Set up dialog views
//        dialogView.findViewById<ImageView>(R.id.dialog_icon)
//            .setImageResource(R.drawable.logo2) // Replace with your icon
//        dialogView.findViewById<TextView>(R.id.dialog_title).text =
//            getString(R.string.allow_location_access)
//        dialogView.findViewById<TextView>(R.id.dialog_message).text =
//            getString(R.string.location_is_required_for_this_app_to_function_properly_please_enable_location_services)
//
//        // Handle button clicks
//        dialogView.findViewById<AppCompatButton>(R.id.btn_settings).setOnClickListener {
//            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//            locationDialog.dismiss()
//        }
//        dialogView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
//            locationDialog.dismiss()
//        }
//
//        locationDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        locationDialog.show()
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun checkCodeInDevices(code: String) {
//        val deviceChecker = DeviceChecker()
//
//        deviceChecker.checkDeviceCode(code) { isMatchFound, matchedDeviceName, matchDeviceID ->
//            if (isMatchFound) {
//                progressDialog.dismiss()
//                Toast.makeText(
//                    requireContext(),
//                    "Code matched with device: $matchedDeviceName",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                editText.text= null
//
//                android.os.Handler(Looper.getMainLooper()).postDelayed({
//
//                    if (matchDeviceID != null) {
//                        openDialog(matchedDeviceName!!,matchDeviceID,)
//                    }
//                },500)
//
//
//
//            } else {
//                progressDialog.dismiss()
//                Toast.makeText(requireContext(), "No matching device found", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createDeviceNode(deviceName: String, currentUserDeviceId: String, matchDeviceID: String, ) {
//        val roomId = matchDeviceID + currentUserDeviceId
//        val database = FirebaseDatabase.getInstance()
//        val deviceRef = database.getReference("Connections")
//            .child(matchDeviceID)
//            .child(roomId) // Path: "Connections/{matchDeviceID}/{roomId}"
//
//        val userRef = database.getReference("devices")
//            .child(currentUserDeviceId) // Path: "devices/{currentUserDeviceId}"
//
//        // Step 1: Update the current user's device information and set parent, child, matchId attributes
//        val userData = mapOf(
//            "parent" to matchDeviceID,
//            "child" to currentUserDeviceId,
//            "deviceName" to deviceName,
//            "androidId" to "9347080e93f705da",  // Your device information
//            "androidVersion" to "13",           // Your device version
//            "manufacturer" to "TECNO",          // Device manufacturer
//            "model" to "TECNO KJ5",             // Device model
//            "sdkVersion" to "33",               // SDK version
//            "uniqueCode" to "75K4J39",          // Unique code
//            "binding" to true,                  // Example of binding status
//            "timestamp" to System.currentTimeMillis()
//        )
//
//        // Update the user's device reference
//        userRef.updateChildren(userData).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Log.d("ConnectFragment", "User device updated successfully with parent, child, and matchId")
//            } else {
//                Log.e("ConnectFragment", "Error updating user device data", task.exception)
//            }
//        }
//
//        // Step 2: Create or update the device node under Connections for the match device
//        val deviceData = mapOf(
//            "deviceName" to deviceName,
//            "binding" to true, // Binding status
//            "parent" to matchDeviceID, // The match device ID
//            "child" to currentUserDeviceId, // The current user device ID
//            "timestamp" to System.currentTimeMillis()
//        )
//
//        // Create or update the device node under Connections
//        deviceRef.setValue(deviceData).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Log.d("ConnectFragment", "Device node created/updated successfully with matchId")
//            } else {
//                Log.e("ConnectFragment", "Error creating/updating device node", task.exception)
//            }
//        }
//    }
//
//    class DeviceChecker {
//
//        private val databaseReference: DatabaseReference =
//            FirebaseDatabase.getInstance().getReference("device")
//
//        // Method to check a single device code
//        fun checkDeviceCode(inputCode: String, onResult: (Boolean, String?, String?) -> Unit) {
//            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    var isMatchFound = false
//                    var matchedDeviceName: String? = null
//                    var matchedDeviceID: String? = null
//
//                    for (deviceSnapshot in snapshot.children) {
//                        val uniqueCode = deviceSnapshot.child("uniqueCode").value?.toString()
//                        if (uniqueCode?.equals(inputCode, ignoreCase = true) == true) {
//                            isMatchFound = true
//                            matchedDeviceName = deviceSnapshot.child("deviceName").value?.toString()
//                            matchedDeviceID = deviceSnapshot.child("androidId").value?.toString()
//                            break
//                        }
//                    }
//
//                    // Pass the result back
//                    onResult(isMatchFound, matchedDeviceName, matchedDeviceID)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    onResult(false, null, null)
//                    println("Database query cancelled: ${error.message}")
//                }
//            })
//        }
//
//        // Method to fetch and print all devices
//        fun fetchAllDevices(onResult: (List<String>) -> Unit) {
//            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val devicePaths = mutableListOf<String>()
//
//                    for (deviceSnapshot in snapshot.children) {
//                        val path = deviceSnapshot.key // Get the path under the "devices" node
//                        val deviceName = deviceSnapshot.child("deviceName").value?.toString()
//                        val uniqueCode = deviceSnapshot.child("uniqueCode").value?.toString()
//
//                        if (path != null) {
//                            devicePaths.add("Path: $path, Device Name: $deviceName, Unique Code: $uniqueCode")
//                        }
//                    }
//
//                    // Pass the list of device paths back
//                    onResult(devicePaths)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    println("Database query cancelled: ${error.message}")
//                    onResult(emptyList())
//                }
//            })
//        }
//
//
//    }
//
//    private fun showSuccessDialog() {
//        val successDialogView =
//            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_success, null)
//        val successDialogBuilder = AlertDialog.Builder(requireContext())
//            .setView(successDialogView)
//            .setCancelable(false)
//
//        val successDialog = successDialogBuilder.create()
//        val btnDismiss: Button = successDialogView.findViewById(R.id.btnDismiss)
//
//        btnDismiss.setOnClickListener {
//
//
//            successDialog.dismiss()
//        }
//
//        // Set success message
//        successDialogView.findViewById<TextView>(R.id.dialog_message).text =
//            getString(R.string.congratulations_you_have_successfully_connected_with_the_device)
//
//        // Ensure the dialog background is transparent
//        successDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//        // Show the dialog
//        successDialog.show()
//    }
//
//
//    // Add this function to fetch current location in ConnectFragment
//    @SuppressLint("MissingPermission")
//    private fun getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Requesting the current location
//            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
//                val location = task.result
//                if (location != null) {
//                    // Fetching latitude and longitude
//                    currentLatLng = LatLng(location.latitude, location.longitude)
//
//                    // You can now use currentLatLng for your logic, for example, logging it
//                    Log.d(
//                        "Location",
//                        "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
//                    )
//
//                    // Optionally, you can display this location on a map or save it to Firebase
//                    // Example: You can add a marker on the map or display the location in a TextView
//                } else {
//                    // Handle case where location is null
//                    Log.e("Location", "Location is null, unable to get current location")
//                    Toast.makeText(
//                        requireContext(),
//                        "Unable to get current location",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        } else {
//            // If permission is not granted, request permission
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                1
//            )
//        }
//    }
//}
