package com.example.locationtracker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var buttonOpen: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mapView = view.findViewById(R.id.mapView)
        buttonOpen = view.findViewById(R.id.btnOpenDialoge)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        buttonOpen.setOnClickListener {
            showCustomDialog()
        }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set initial location
        val initialLocation = LatLng(37.7749, -122.4194) // San Francisco
        googleMap.addMarker(MarkerOptions().position(initialLocation).title("San Francisco"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 12f))
    }

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

        // Set click listener to close the dialog
        closeButton.setOnClickListener {
            Toast.makeText(requireContext(), "Close button clicked", Toast.LENGTH_SHORT).show()
            Log.d("Dialog", "Close button clicked")
            alertDialog.dismiss() // Dismiss the dialog
        }
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

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
