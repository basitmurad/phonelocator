package com.example.locationtracker.fragments
import Checker
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.locationtracker.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.example.locationtracker.helper.UserHelpers
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class ConnectFragment : Fragment() {

    private lateinit var appCompatButton2: Button
    private lateinit var editText: EditText
    private lateinit var progressDialog: ProgressDialog
    private lateinit var permissionChecker: Checker
    private lateinit var batteryPercentage: String
    private lateinit var currentLatLng: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userHelpers: UserHelpers
    private lateinit var enterCode:String
    private lateinit var parentDeviceID:String

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connect, container, false)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please wait, locating your device")
        progressDialog.setCancelable(false)
        initializeLocation()

        userHelpers = UserHelpers(requireContext())
        permissionChecker = Checker(requireContext())

        appCompatButton2 = view.findViewById(R.id.appCompatButton2)
        editText = view.findViewById(R.id.editText)

        appCompatButton2.setOnClickListener {
            val code = editText.text.toString().trim()

            if (code.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a code", Toast.LENGTH_SHORT).show()
            } else {
                val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

                fun checkAndPromptForGPS() {
                    val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                    if (!isGPSEnabled) {
                        // Show custom dialog to prompt user to enable GPS
                        val dialog = Dialog(requireContext())
                        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_generic_disabled, null)
                        dialog.setContentView(dialogView)
                        dialog.setCancelable(false)

                        val positiveButton: AppCompatButton = dialogView.findViewById(R.id.btn_settings)
                        val negativeButton: AppCompatButton = dialogView.findViewById(R.id.btn_cancel)
                        dialogView.findViewById<TextView>(R.id.dialog_title).text = getString(R.string.allow_location_access)
                        dialogView.findViewById<TextView>(R.id.dialog_message).text = getString(R.string.location_is_required_for_this_app_to_function_properly_please_enable_location_services)

                        positiveButton.setOnClickListener {
                            // Open location settings
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            dialog.dismiss()

                            // Re-check GPS status after returning from settings
                            android.os.Handler(Looper.getMainLooper()).postDelayed({
                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    checkAndPromptForGPS()
                                } else {
                                    Toast.makeText(requireContext(), "GPS is now enabled!", Toast.LENGTH_SHORT).show()
                                }
                            }, 1000)
                        }

                        negativeButton.setOnClickListener {
                            dialog.dismiss()
                            // Optionally, recheck after a delay
                            android.os.Handler(Looper.getMainLooper()).postDelayed({
                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    checkAndPromptForGPS()
                                }
                            }, 1000)
                        }

                        dialog.show()
                    } else {
                        // GPS is enabled, proceed with Firebase query
                        progressDialog.show()
                        editText.filters = arrayOf(InputFilter.AllCaps())


                        val databaseReference = FirebaseDatabase.getInstance().getReference("devices")
                        databaseReference.orderByChild("uniqueCode").equalTo(code)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    progressDialog.dismiss()
                                    if (snapshot.exists()) {
                                        for (child in snapshot.children) {
                                            val androidVersion = child.child("androidVersion").value?.toString() ?: "N/A"
                                            val deviceName = child.child("deviceName").value?.toString() ?: "N/A"
                                            val deviceID = child.child("deviceId").value?.toString() ?: "N/A"
                                            val manufacturer = child.child("manufacturer").value?.toString() ?: "N/A"
                                            val model = child.child("model").value?.toString() ?: "N/A"
                                            val sdkVersion = child.child("sdkVersion").value?.toString() ?: "N/A"

                                            editText.text= null
                                            openDialog(deviceName, deviceID)
                                            break
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Invalid Code", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    progressDialog.dismiss()
                                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }

                checkAndPromptForGPS()
            }
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDialog(deviceName: String, matchDeviceID: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.confirm_dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext()).setView(dialogView).setCancelable(false)

        val alertDialog = dialogBuilder.create()
        val code: TextView = dialogView.findViewById(R.id.descriptionTextView122)
        val btnNo: Button = dialogView.findViewById(R.id.button1)
        val btnConfirm: Button = dialogView.findViewById(R.id.button2)

        btnConfirm.setOnClickListener {
            if (!permissionChecker.isLocationEnabled()) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                return@setOnClickListener
            }

            val currentLatLng = this.currentLatLng
            if (currentLatLng == null ) {
                Toast.makeText(requireContext(), "Location data is unavailable.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUserDeviceId = userHelpers.getAndroidId()
            val currentTime = userHelpers.getCurrentTime()
            val currentDate = userHelpers.getCurrentDate()
            val batteryPercentage = getBatteryPercentage()

            println("Data us $currentUserDeviceId and $matchDeviceID")

            saveDeviceData(currentUserDeviceId, currentLatLng, batteryPercentage, currentTime, currentDate)

            val databaseReference = FirebaseDatabase.getInstance().getReference("Connection")
            val deviceData = saveDeviceData(currentUserDeviceId, currentLatLng, batteryPercentage, currentTime, currentDate)

            databaseReference.child(matchDeviceID).child(currentUserDeviceId)
                .setValue(deviceData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to save data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

//            val databaseReference = FirebaseDatabase.getInstance().getReference("Connection")
//            databaseReference.child(matchDeviceID).child(currentUserDeviceId)
//                .setValue(saveDeviceData(currentUserDeviceId, currentLatLng, batteryPercentage, currentTime, currentDate))
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(requireContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "Failed to save data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }

            alertDialog.dismiss()
        }

        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        code.text = deviceName
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        alertDialog.window?.apply {
            val layoutParams = attributes
            val margin = 50
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            setLayout(resources.displayMetrics.widthPixels - 2 * margin, ViewGroup.LayoutParams.WRAP_CONTENT)
            attributes = layoutParams
        }
    }

    private fun saveDeviceData(deviceId: String, latLng: LatLng?, battery: String, time: String, date: String): Map<String, Any> {
        return mapOf(
            "deviceId" to deviceId,
            "latitude" to (latLng?.latitude ?: 0.0),
            "longitude" to (latLng?.longitude ?: 0.0),
            "battery" to battery,
            "time" to time,
            "date" to date
        )
    }

    private fun getBatteryPercentage(): String {
        val batteryManager = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return "$batteryLevel%"
    }

    private fun initializeLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Location permissions are not granted", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLatLng = LatLng(location.latitude, location.longitude)
            } else {
                Toast.makeText(requireContext(), "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
