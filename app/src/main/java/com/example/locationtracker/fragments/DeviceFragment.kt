
package com.example.locationtracker.fragments

import DeviceAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Geocoder
import android.provider.Settings
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ProgressBar

private const val s = "No Data Available"

class DeviceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId", "HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_device, container, false)

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recuycelr)
        recyclerView.layoutManager = LinearLayoutManager(context)


        // Set up progress bar for loading indication
        progressBar = view.findViewById(R.id.progress_bar)  // Ensure the progress bar exists in your layout XML

        // Get the Android device ID
        val androidId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        // Show progress bar before data fetch
        progressBar.visibility = View.VISIBLE


        return view
    }


    private fun convertTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    private fun showNoDataDialog() {
        if (!isAdded) {
            // Fragment is not attached, return early
            return
        }
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(R.string.no_data_available)
        dialogBuilder.setMessage("No device connections found. Please check your connection or try again later.")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun getPlaceNameFromLatLong(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1) // Fetch 1 result
            if (addresses?.isNotEmpty() == true) {
                val address = addresses[0]
                "${address.locality}, ${address.countryName}" // Example: "San Francisco, United States"
            } else {
                "Unknown Location"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error fetching location"
        }
    }

}
