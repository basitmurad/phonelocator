//package com.example.locationtracker.fragments
//
//import android.annotation.SuppressLint
//import android.app.AlertDialog
//import android.app.ProgressDialog
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.provider.Settings
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.CheckBox
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.locationtracker.R
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//import android.Manifest
//import android.os.Build
//import androidx.annotation.RequiresApi
//
//class ConnectFragment : Fragment() {
//
//private lateinit var appCompatButton2:Button
//private lateinit var editext:EditText
//    private lateinit var progressDialog: ProgressDialog
//    private val REQUEST_CODE_PERMISSION = 101
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_connect, container, false)
//        progressDialog = ProgressDialog(requireContext())
//        progressDialog.setMessage("Please wait location your device")
//        progressDialog.setCancelable(false)
//
//        appCompatButton2= view.findViewById(R.id.appCompatButton2)
//        editext= view.findViewById(R.id.editText)
//
//
//        appCompatButton2.setOnClickListener {
//            progressDialog.show()
//
//            val code = editext.text.toString().trim()
//
//            if (code.isEmpty()) {
//                Toast.makeText(requireContext(), "Please enter a code", Toast.LENGTH_SHORT).show()
//                progressDialog.dismiss()
//            } else {
//                // Check if the code matches any device
//                checkCodeInDevices(code)
//
//                // Fetch all devices and print their paths
//                val deviceChecker = DeviceChecker()
//                deviceChecker.fetchAllDevices { devicePaths ->
//                    progressDialog.dismiss()
//                    if (devicePaths.isNotEmpty()) {
//                        devicePaths.forEach { path ->
//                            println(path) // Log each device path
//                        }
//                        Toast.makeText(requireContext(), "Device paths logged in console", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "No devices found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//
//   return view
//    }
//
//
//
//
//    @SuppressLint("MissingInflatedId")
//    private fun openDialoge(deviceName:String) {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.confirm_dialog_layout, null)
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
//        btnConfirm.setOnClickListener {
//            // Dismiss the dialog
//            alertDialog.dismiss()
//            showAutoStartDialog()
//
//        }
//
//
//
//
//        code.text =deviceName
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
//
//
//        }
//
//
//
//
//
//    }
//    // Example for requesting permissions like Motion Tracking
//    @RequiresApi(Build.VERSION_CODES.Q)
//    private fun requestMotionTrackingPermission() {
//        val permission = android.Manifest.permission.ACTIVITY_RECOGNITION
//        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE_PERMISSION)
//        }
//    }
//
//    private fun showAutoStartDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Auto Start Permission")
//            .setMessage("To ensure the app runs smoothly, it needs permission to start automatically when the device is turned on. Please enable Auto Start in your device settings.")
//            .setCancelable(false)
//            .setPositiveButton("Go to Settings") { _, _ ->
//                // Open the app's settings page
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = Uri.parse("package:${requireContext().packageName}")
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
//    private fun showProtectedAppDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Protected App Permission")
//            .setMessage("For the app to run in the background without being killed by the system, please enable it as a protected app in your device settings.")
//            .setCancelable(false)
//            .setPositiveButton("Go to Settings") { _, _ ->
//                // Open the app's settings page
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = Uri.parse("package:${requireContext().packageName}")
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
//    private fun showMotionTrackingDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Motion Tracking Permission")
//            .setMessage("To track your movements, the app requires permission to access motion sensors. Please enable Motion Tracking in your device settings.")
//            .setCancelable(false)
//            .setPositiveButton("Go to Settings") { _, _ ->
//                // Open the settings page to enable necessary permissions
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = Uri.parse("package:${requireContext().packageName}")
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
//
//
//
//    private fun checkCodeInDevices(code: String) {
//        val deviceChecker = DeviceChecker()
//
//        deviceChecker.checkDeviceCode(code) { isMatchFound, matchedDeviceName ->
//            if (isMatchFound) {
//                progressDialog.dismiss()
//                Toast.makeText(requireContext(), "Code matched with device: $matchedDeviceName", Toast.LENGTH_SHORT).show()
//
//                openDialoge("$matchedDeviceName")
//
//            } else {
//                progressDialog.dismiss()
//                Toast.makeText(requireContext(), "No matching device found", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//
//
//    class DeviceChecker {
//
//        private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("devices")
//
//        // Method to check a single device code
//        fun checkDeviceCode(inputCode: String, onResult: (Boolean, String?) -> Unit) {
//            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    var isMatchFound = false
//                    var matchedDeviceName: String? = null
//
//                    for (deviceSnapshot in snapshot.children) {
//                        val uniqueCode = deviceSnapshot.child("uniqueCode").value?.toString()
//                        if (uniqueCode == inputCode) {
//                            isMatchFound = true
//                            matchedDeviceName = deviceSnapshot.child("deviceName").value?.toString()
//                            break
//                        }
//                    }
//
//                    // Pass the result back
//                    onResult(isMatchFound, matchedDeviceName)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    onResult(false, null)
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
//    }
//
//
//
//
//}
//
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
            alertDialog.dismiss()
            showMotionTrackingDialog()
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

    // Request permissions like Motion Tracking
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestMotionTrackingPermission() {
        val permission = android.Manifest.permission.ACTIVITY_RECOGNITION
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE_PERMISSION)
        }
    }

    private fun showAutoStartDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Auto Start Permission")
            .setMessage("To ensure the app runs smoothly, it needs permission to start automatically when the device is turned on. Please enable Auto Start in your device settings.")
            .setCancelable(false)
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open the app's settings page
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showProtectedAppDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Protected App Permission")
            .setMessage("For the app to run in the background without being killed by the system, please enable it as a protected app in your device settings.")
            .setCancelable(false)
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open the app's settings page
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showMotionTrackingDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Motion Tracking Permission")
            .setMessage("To track your movements, the app requires permission to access motion sensors. Please enable Motion Tracking in your device settings.")
            .setCancelable(false)
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open the settings page to enable necessary permissions
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun checkCodeInDevices(code: String) {
        val deviceChecker = DeviceChecker()

        deviceChecker.checkDeviceCode(code) { isMatchFound, matchedDeviceName ->
            if (isMatchFound) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Code matched with device: $matchedDeviceName", Toast.LENGTH_SHORT).show()

                openDialog(matchedDeviceName!!)
            } else {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "No matching device found", Toast.LENGTH_SHORT).show()
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
