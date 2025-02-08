package com.example.locationtracker.fragments

import Checker
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
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

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.helper.UserHelpers
import com.example.locationtracker.models.ConnectionRequest
import com.example.locationtracker.models.ConnectionResponse
import com.example.locationtracker.models.DeviceProfileResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConnectFragment : Fragment() {

    private lateinit var appCompatButton2: Button
    private lateinit var scanButton: Button
    private lateinit var editText: EditText
    private lateinit var progressDialog: ProgressDialog
    private lateinit var permissionChecker: Checker
    private lateinit var currentLatLng: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userHelpers: UserHelpers


    private val qrScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED // Reset orientation after scan

        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val scanResult = IntentIntegrator.parseActivityResult(result.resultCode, data)

            if (scanResult.contents != null) {
                Log.d("QRScanner", "Scanned QR Code: ${scanResult.contents}") // ✅ Show in Logs

                // Extract ID from URL and set it in EditText
                val extractedId = extractIdFromUrl(scanResult.contents)
                editText.setText(extractedId)

            } else {
                Log.d("QRScanner", "Scan cancelled or failed.") // ✅ Show in Logs
                Toast.makeText(requireContext(), "Scan cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startQRScanner() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Force portrait mode

        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR Code")
        integrator.setCameraId(0) // Use the back camera
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        qrScannerLauncher.launch(integrator.createScanIntent())
    }

    private fun extractIdFromUrl(url: String): String {
        return url.substringAfterLast("=") // ✅ Extracts everything after the last '='
    }




    @SuppressLint("ServiceCast", "MissingInflatedId")
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
        scanButton = view.findViewById(R.id.appCompatButton234545)
        editText = view.findViewById(R.id.editText12)

        appCompatButton2.setOnClickListener {
            val  connectionID = editText.text.toString().trim()
            if (connectionID.isEmpty()){ Toast.makeText(requireContext(), "Please enter a code", Toast.LENGTH_SHORT).show()

            }
            else{
                fetchDeviceProfile(connectionID)
            }

        }

        scanButton.setOnClickListener{
            startQRScanner()

        }

        return view
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDialog(deviceName: String, connectionID: String) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.confirm_dialog_layout, null)
        val dialogBuilder =
            AlertDialog.Builder(requireContext()).setView(dialogView).setCancelable(false)

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

//            val currentLatLng = this.currentLatLng

            val currentUserDeviceId = userHelpers.getAndroidId()
            val currentTime = userHelpers.getCurrentTime()
            val currentDate = userHelpers.getCurrentDate()
            val batteryPercentage = getBatteryPercentage()

            println("Data us $currentUserDeviceId and $connectionID")

            Log.d("data is " , " current user id$currentUserDeviceId and connectioID is $connectionID")


            createConnection(currentUserDeviceId, connectionID)
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
            setLayout(
                resources.displayMetrics.widthPixels - 2 * margin,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            attributes = layoutParams
        }
    }

    private fun getBatteryPercentage(): String {
        val batteryManager =
            requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
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
            Toast.makeText(
                requireContext(),
                "Location permissions are not granted",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLatLng = LatLng(location.latitude, location.longitude)
            } else {
                Toast.makeText(requireContext(), "Unable to fetch location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun fetchDeviceProfile(deviceId: String) {
        RetrofitClient.apiService.getDeviceProfile(deviceId).enqueue(object : Callback<DeviceProfileResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<DeviceProfileResponse>, response: Response<DeviceProfileResponse>) {
                if (response.isSuccessful) {
                    val deviceProfileResponse = response.body()
                    if (deviceProfileResponse != null && deviceProfileResponse.success) {
                        val profile = deviceProfileResponse.profile
                        if (profile != null) {
                            val deviceName = profile.deviceName
                            val connectionID = profile.deviceId

                            Log.d("DeviceProfile", "Device Name: $deviceName")

                            // Open dialog with retrieved device name and ID
                            openDialog(deviceName,connectionID )
                        } else {
                            Toast.makeText(requireContext(), "Device profile not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch device profile", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("DeviceProfile", "Error Code: ${response.code()} - ${response.message()}")
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeviceProfileResponse>, t: Throwable) {
                Log.e("DeviceProfile", "Failure: ${t.message}")
                Toast.makeText(requireContext(), "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun createConnection(deviceId: String, connectionId: String) {
        // Prepare the request body
        val connectionRequest = ConnectionRequest(deviceId, connectionId)

        // Call the API
        RetrofitClient.apiService.createConnection(connectionRequest)
            .enqueue(object : Callback<ConnectionResponse> {
                override fun onResponse(
                    call: Call<ConnectionResponse>,
                    response: Response<ConnectionResponse>
                ) {
                    if (response.isSuccessful) {
                        // Handle success
                        val connectionResponse = response.body()
                        if (connectionResponse != null && connectionResponse.success) {
                            // Successfully created the connection
                            Log.d("DeviceProfile", ": ${connectionResponse.message}")

                            println("Connection Created: ${connectionResponse.message}")
                            println("Connection ID: ${connectionResponse.connection?._id}")
                        } else {
                            Log.d("DeviceProfile", ": ${connectionResponse?.message}")

                            // Handle the case where the connection wasn't created
                            println("Connection: ${connectionResponse?.message}")
                        }
                    } else {
                        // Handle unsuccessful response
                        println("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ConnectionResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to create connection: ${t.message}")
                }
            })
    }


}
