package com.example.locationtracker.fragments
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.locationtracker.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import android.Manifest
import android.content.Context
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi

class ConnectFragment : Fragment() {

    private lateinit var appCompatButton2: Button
    private lateinit var editText: EditText
    private lateinit var progressDialog: ProgressDialog
    private val REQUEST_CODE_PERMISSION = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connect, container, false)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please wait, locating your device")
        progressDialog.setCancelable(false)

        appCompatButton2 = view.findViewById(R.id.appCompatButton2)
        editText = view.findViewById(R.id.editText)

        appCompatButton2.setOnClickListener {
            progressDialog.show()
            editText.filters = arrayOf(InputFilter.AllCaps())

            val code = editText.text.toString().trim()


            if (code.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a code", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            } else {
                // Check if the code matches any device
                checkCodeInDevices(code)

                // Fetch all devices and print their paths
                val deviceChecker = DeviceChecker()
                deviceChecker.fetchAllDevices { devicePaths ->
                    progressDialog.dismiss()
                    if (devicePaths.isNotEmpty()) {
                        devicePaths.forEach { path ->
                            println(path) // Log each device path
                        }
                        Toast.makeText(requireContext(), "Device paths logged in console", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "No devices found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

    @SuppressLint("MissingInflatedId")
    private fun openDialog(deviceName: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.confirm_dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        val code: TextView = dialogView.findViewById(R.id.descriptionTextView122)
        val btnNo: Button = dialogView.findViewById(R.id.button1)
        val btnConfirm: Button = dialogView.findViewById(R.id.button2)



        btnConfirm.setOnClickListener {
            // Dismiss the dialog
            if (isBatteryOptimizationDisabled()) {
                // Proceed if optimization is disabled
                alertDialog.dismiss()
                showMotionTrackingDialog()
            } else {
                // Show custom dialog to disable battery optimization
                showBatteryOptimizationDialog()
            }
        }
        btnNo.setOnClickListener{
            alertDialog.dismiss()
        }

        code.text = deviceName

        // Ensure the dialog background is transparent
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Show the dialog first to get its window
        alertDialog.show()

        // Set dialog margins
        val window = alertDialog.window
        if (window != null) {
            val layoutParams = window.attributes
            val margin = 50 // Set margin in pixels (e.g., 50px)
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.setLayout(
                (resources.displayMetrics.widthPixels - 2 * margin),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.attributes = layoutParams
        }
    }

    private fun isBatteryOptimizationDisabled(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = requireContext().getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            return !powerManager.isIgnoringBatteryOptimizations(requireContext().packageName)
        }
        return true  // Battery optimization check is not needed for versions before M
    }
    private fun showBatteryOptimizationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Battery Optimization")
            .setMessage("To ensure the app runs smoothly, please disable battery optimization.")
            .setCancelable(false)
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open the settings page to allow the user to disable battery optimization
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivityForResult(intent, 1)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(requireContext(), "Please disable battery optimization for optimal performance.", Toast.LENGTH_SHORT).show()
            }

        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showMotionTrackingDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_motion_tracking_dialog, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Set click listeners for the buttons
        val btnGoToSettings = dialogView.findViewById<Button>(R.id.btnGoToSettings)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnGoToSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${requireContext().packageName}")
            }
            startActivity(intent)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun checkCodeInDevices(code: String) {
        val deviceChecker = DeviceChecker()

        deviceChecker.checkDeviceCode(code) { isMatchFound, matchedDeviceName ->
            if (isMatchFound) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Code matched with device: $matchedDeviceName", Toast.LENGTH_SHORT).show()
                if (matchedDeviceName != null) {

                    val  deviceId  = getAndroidId();

                    createDeviceNode(code, matchedDeviceName , deviceId)
                }

                openDialog(matchedDeviceName!!)
            } else {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "No matching device found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return requireContext().contentResolver.let {
            android.provider.Settings.Secure.getString(it, android.provider.Settings.Secure.ANDROID_ID)
        }
    }

    private fun createDeviceNode(deviceId: String, deviceName: String , currentUser: String) {
       val roomId = deviceId+ currentUser;
        val database = FirebaseDatabase.getInstance()
        val deviceRef = database.getReference("parentchild").child(deviceId).child(roomId) // Path: "devices/{deviceId}"

        // Creating a new node with 'binding' and other relevant information
        val deviceData = mapOf(
            "deviceName" to deviceName,
            "binding" to true, // Example of binding status, change as needed
            "parent" to deviceId, // Example of binding status, change as needed
            "child" to deviceId, // Example of binding status, change as needed
            "timestamp" to System.currentTimeMillis() // Add timestamp if needed
        )


        println("The data is  $deviceId of  the parent and child device ID is $currentUser , and the device name is $deviceId")
        deviceRef.setValue(deviceData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ConnectFragment", "Device node created successfully")
            } else {
                Log.e("ConnectFragment", "Error creating device node", task.exception)
            }
        }
    }

    class DeviceChecker {

        private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("devices")

        // Method to check a single device code
        fun checkDeviceCode(inputCode: String, onResult: (Boolean, String?) -> Unit) {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isMatchFound = false
                    var matchedDeviceName: String? = null

                    for (deviceSnapshot in snapshot.children) {
                        val uniqueCode = deviceSnapshot.child("uniqueCode").value?.toString()
                        if (uniqueCode == inputCode) {
                            isMatchFound = true
                            matchedDeviceName = deviceSnapshot.child("deviceName").value?.toString()
                            break
                        }
                    }

                    // Pass the result back
                    onResult(isMatchFound, matchedDeviceName)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(false, null)
                    println("Database query cancelled: ${error.message}")
                }
            })
        }

        // Method to fetch and print all devices
        fun fetchAllDevices(onResult: (List<String>) -> Unit) {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val devicePaths = mutableListOf<String>()

                    for (deviceSnapshot in snapshot.children) {
                        val path = deviceSnapshot.key // Get the path under the "devices" node
                        val deviceName = deviceSnapshot.child("deviceName").value?.toString()
                        val uniqueCode = deviceSnapshot.child("uniqueCode").value?.toString()

                        if (path != null) {
                            devicePaths.add("Path: $path, Device Name: $deviceName, Unique Code: $uniqueCode")
                        }
                    }

                    // Pass the list of device paths back
                    onResult(devicePaths)
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Database query cancelled: ${error.message}")
                    onResult(emptyList())
                }
            })
        }
    }
}
