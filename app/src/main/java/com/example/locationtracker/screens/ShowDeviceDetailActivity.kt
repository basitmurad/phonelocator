
package com.example.locationtracker.screens

import DeviceDetailAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtracker.R
import com.example.locationtracker.databinding.ActivityShowDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ShowDeviceDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityShowDetailBinding
    private lateinit var mMap: GoogleMap
    private lateinit var database: DatabaseReference
    private lateinit var adapter: DeviceDetailAdapter
    private var currentMarker: Marker? = null // Variable to store the current marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference

        binding.btnBack.setOnClickListener{finish()}
        // Retrieve latitude and longitude from the Intent
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val childId = intent.getStringExtra("childId")
        val parentID = intent.getStringExtra("parentID")

        val latLng = LatLng(latitude, longitude)

        Log.d("Information", latLng.toString())
        Log.d("Information", childId.toString())
        Log.d("Information", parentID.toString())

        binding.mapView444.onCreate(savedInstanceState)
        binding.mapView444.getMapAsync(this)

        // Fetch device data and pass the IDs
        childId?.let {
            if (parentID != null) {
                fetchDeviceData(parentID, it)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        // Retrieve latitude and longitude from the Intent
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val latLng = LatLng(latitude, longitude)

        // Create a custom BitmapDescriptor from a drawable resource
//        val customMarker = BitmapDescriptorFactory.fromResource(R.drawable.logo2)
        val customMarker = createCustomMarker(R.drawable.logo2, 70, 70) // Resize marker to 100x100

        // Add a marker at the retrieved location with the custom icon
         currentMarker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Device Location") // Title for the marker
                .snippet("Latitude: $latitude, Longitude: $longitude") // Optional snippet
                .icon(customMarker) // Set the custom marker icon
        )

        // Move the camera to the marker position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f)) // Zoom level is adjustable
    }
    private fun createCustomMarker(resId: Int, width: Int, height: Int): BitmapDescriptor {
        // Decode the resource into a Bitmap
        val bitmap = BitmapFactory.decodeResource(resources, resId)

        // Scale the Bitmap to the desired width and height
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

        // Return a BitmapDescriptor using the scaled Bitmap
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }



    private fun fetchDeviceData(parentID: String, childID: String) {
        // Fetch the device name first
        fetchDeviceName(childID) { deviceName ->


            val connectionRef = database.child("Connection").child(parentID).child(childID)

            // Retrieve the data from Firebase
            connectionRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val deviceList = mutableListOf<DeviceDetailAdapter.ItemData>()

                    snapshot.children.forEach { childSnapshot ->
                        val battery = childSnapshot.child("battery").getValue(String::class.java) ?: "Unknown"
                        val date = childSnapshot.child("date").getValue(String::class.java) ?: "Unknown"
                        val latitude = childSnapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                        val longitude = childSnapshot.child("longitude").getValue(Double::class.java) ?: 0.0
                        val time = childSnapshot.child("time").getValue(String::class.java) ?: "Unknown"

                        // Log the fetched data for debugging
                        Log.d("Device", "Battery: $battery")
                        Log.d("Device", "ChildID: $childID")
                        Log.d("Device", "Date: $date")
                        Log.d("Device", "Coordinates: $latitude, $longitude")
                        Log.d("Device", "ParentID: $parentID")
                        Log.d("Device", "Time: $time")

                        // Add the data to the list
                        deviceList.add(
                            DeviceDetailAdapter.ItemData(
                                circleText = deviceName.firstOrNull()?.toString() ?: "", // First char of device name
                                title = deviceName, // Use device name as the title
                                connectedNow = battery, // Show battery info as connected status
                                date = "$date $time", // Combine date and time
                                lastMap = "$latitude, $longitude", // Use combined latitude and longitude string
                                battery = battery,
                                latLong = LatLng(latitude, longitude)
                            )
                        )
                    }

                    // Set the data to the adapter with the onClickListener
                    adapter = DeviceDetailAdapter(deviceList, object : DeviceDetailAdapter.OnItemClickListener {
                        override fun onButtonClick(latLng: LatLng) {
                            // Remove the previous marker if it exists
                            currentMarker?.remove()
                            val customMarker = createCustomMarker(R.drawable.logo2, 70, 70) // Resize marker to 100x100

                            // Add a new marker at the clicked location
                            currentMarker = mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title("Device Location") // Title for the marker
                                    .snippet("Latitude: ${latLng.latitude}, Longitude: $latLng.longitude")
                                    .icon(customMarker)
                            )

                            // Move the camera to the new marker position with smooth animation
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }
                    })
                    binding.deviceREcycler.adapter = adapter
                    binding.deviceREcycler.layoutManager = LinearLayoutManager(this)
                }
            }.addOnFailureListener { exception ->
                // Handle error
                exception.printStackTrace()
            }
        }
    }

    private fun fetchDeviceName(childID: String, callback: (String) -> Unit) {
        val deviceRef = database.child("devices").child(childID)
        deviceRef.child("deviceName").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val deviceName = snapshot.getValue(String::class.java) ?: "Unknown Device"
                callback(deviceName)
            } else {
                callback("Unknown Device")
            }
        }.addOnFailureListener {
            it.printStackTrace()
            callback("Unknown Device")
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView444.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView444.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView444.onDestroy()
    }
}
