
package com.example.locationtracker

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var buttonOpen: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var deviceReference: DatabaseReference
    private lateinit var androidId: String
    private lateinit var uniqueCode: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mapView = view.findViewById(R.id.mapView)
        buttonOpen = view.findViewById(R.id.btnOpenDialoge)

        database = FirebaseDatabase.getInstance()
        deviceReference = database.getReference("devices")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Get Android ID
        androidId = getAndroidId()

        // Fetch device information
        fetchDeviceInformation()

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        buttonOpen.setOnClickListener {
            showCustomDialog()
        }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Request current location
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        // Check if permission is granted (you need to handle permissions properly)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            } else {
                Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_custom, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Ensuring the dialog background is transparent
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Show the dialog
        alertDialog.show()

        // Find the close button
        val closeButton: ImageView = dialogView.findViewById(R.id.btnClose)
        val code: TextView = dialogView.findViewById(R.id.textView6)
        val btnCopy: TextView = dialogView.findViewById(R.id.textView10)
        val btnShare: LinearLayout = dialogView.findViewById(R.id.btnShare)

        // Set the unique code text
        code.text = uniqueCode

        // Share button click listener
        btnShare.setOnClickListener {
            shareCode(uniqueCode)
        }

        // Copy button click listener
        btnCopy.setOnClickListener {
            copyToClipboard(uniqueCode)
            Toast.makeText(requireContext(), "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
        }

        // Close button click listener
        closeButton.setOnClickListener {
            Toast.makeText(requireContext(), "Close button clicked", Toast.LENGTH_SHORT).show()
            Log.d("Dialog", "Close button clicked")
            alertDialog.dismiss() // Dismiss the dialog
        }

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

    private fun copyToClipboard(uniqueCode: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Unique Code", uniqueCode)
        clipboard.setPrimaryClip(clip)
    }

    private fun shareCode(uniqueCode: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Here is my unique code: $uniqueCode")
        }

        // Show the chooser dialog for sharing options
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return requireContext().contentResolver.let {
            android.provider.Settings.Secure.getString(it, android.provider.Settings.Secure.ANDROID_ID)
        }
    }

    private fun fetchDeviceInformation() {
        // Show a loading message or progress bar if required

        // Fetch device information from Firebase
        deviceReference.child(androidId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val deviceName = snapshot.child("deviceName").getValue(String::class.java) ?: "N/A"
                    val manufacturer = snapshot.child("manufacturer").getValue(String::class.java) ?: "N/A"
                    val model = snapshot.child("model").getValue(String::class.java) ?: "N/A"
                    val androidVersion = snapshot.child("androidVersion").getValue(String::class.java) ?: "N/A"
                    val sdkVersion = snapshot.child("sdkVersion").getValue(Int::class.java) ?: -1
                    uniqueCode = snapshot.child("uniqueCode").getValue(String::class.java) ?: "N/A"

                    // Display the information in the UI or log it
                    Log.d("Device Info", "Device Name: $deviceName, Unique Code: $uniqueCode")

                } else {
                    Toast.makeText(requireContext(), "No device information found!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch device information: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
