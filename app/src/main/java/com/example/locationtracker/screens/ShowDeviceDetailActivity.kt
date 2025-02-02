
package com.example.locationtracker.screens

import DeviceDetailAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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


class ShowDeviceDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityShowDetailBinding
    private lateinit var mMap: GoogleMap
    private lateinit var adapter: DeviceDetailAdapter
    private var currentMarker: Marker? = null // Variable to store the current marker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
