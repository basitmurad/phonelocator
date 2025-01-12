
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ProgressBar

private const val s = "No Data Available"

class DeviceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var database: DatabaseReference
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

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Set up progress bar for loading indication
        progressBar = view.findViewById(R.id.progress_bar)  // Ensure the progress bar exists in your layout XML

        // Get the Android device ID
        val androidId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        // Show progress bar before data fetch
        progressBar.visibility = View.VISIBLE

        // Fetch data from Firebase based on the Android ID
        fetchDeviceData(androidId)

        return view
    }

    private fun fetchDeviceData(androidId: String) {
        val connectionRef = database.child("Connection").child(androidId)

        connectionRef.get().addOnSuccessListener { snapshot ->
            progressBar.visibility = View.GONE // Hide progress bar after fetching data

            if (snapshot.exists()) {
                Log.d("DeviceFragment", "Snapshot exists: $snapshot")

                val itemList = mutableListOf<DeviceAdapter.ItemData>()

                // Create a GenericTypeIndicator for a Map
                val deviceDataType = object : GenericTypeIndicator<Map<String, Any>>() {}

                // Loop through each child at the parent level
                for (parentSnapshot in snapshot.children) {
                    // Get the first child snapshot of the parent (childID)
                    val firstChildSnapshot = parentSnapshot.children.firstOrNull()

                    // If the first child exists, fetch its data
                    if (firstChildSnapshot != null) {
                        // Using GenericTypeIndicator to safely retrieve Map data
                        val deviceData = firstChildSnapshot.getValue(deviceDataType)

                        if (deviceData != null) {
                            val battery = deviceData["battery"] as? String ?: "Unknown"
                            val childID = deviceData["childID"] as? String ?: "Unknown"
                            val date = deviceData["date"] as? String ?: "Unknown"
                            val time = deviceData["time"] as? String ?: "Unknown"
                            val latitude = deviceData["latitude"] as? Double ?: 0.0
                            val longitude = deviceData["longitude"] as? Double ?: 0.0
                            val parentID = deviceData["parentID"] as? String ?: "Unknown"

                            // Fetch location name from latitude and longitude
                            val locationName = getPlaceNameFromLatLong(latitude, longitude)

                            // Fetch child name
                            fetchChildName(childID) { childName ->




                                val itemData = DeviceAdapter.ItemData(
                                    circleText = childName.take(1),  // You can set the text accordingly
                                    battery = battery,
                                    title = childName,     // Set the title dynamically if needed
                                    connectedNow = "Yes",       // Set based on your data
                                    date = date,
                                    lastMap = locationName,  // Set the location name dynamically
                                    latLong = LatLng(latitude,longitude),
                                    childId = childID,
                                    parentId = parentID
                                )
                                itemList.add(itemData)

                                deviceAdapter = DeviceAdapter(itemList)
                                recyclerView.adapter = deviceAdapter
                                Log.d("data" ,"$itemData")
                            }
                        }
                    }
                    // If you only want the first child, break the loop here
                    break
                }
            } else {
                Log.d("DeviceFragment", "No data available for this device.")
                showNoDataDialog()  // Show no data dialog
            }
        }.addOnFailureListener { exception ->
            progressBar.visibility = View.GONE  // Hide progress bar in case of failure
            Log.e("DeviceFragment", "Failed to fetch device data", exception)
            showNoDataDialog()  // Show no data dialog on failure
        }
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

    private fun fetchChildName(childID: String, callback: (String) -> Unit) {
        val childRef = database.child("devices").child(childID)

        childRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Get child name or default to "Unknown"
                val childName = snapshot.child("deviceName").getValue(String::class.java) ?: "Unknown"
                callback(childName)
            } else {
                callback("Unknown")
            }
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            callback("Error fetching name")
        }
    }
}
