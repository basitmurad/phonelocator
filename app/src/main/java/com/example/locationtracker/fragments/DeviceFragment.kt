
package com.example.locationtracker.fragments

import DeviceAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.provider.Settings
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DeviceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var database: DatabaseReference

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

        // Get the Android device ID
        val androidId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        // Fetch data from Firebase based on the Android ID
        fetchDeviceData(androidId)

        return view
    }



    private fun fetchDeviceData(androidId: String) {
        // Database reference to "Connections" node and the specific Android ID child
        val connectionRef = database.child("Connection").child(androidId)

        // Retrieve the data from Firebase
        connectionRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // List to store device data
                val deviceList = mutableListOf<DeviceAdapter.ItemData>()

                // Iterate through the second-level children of the node
                snapshot.children.forEach { childSnapshot ->
                    val battery = childSnapshot.child("battery").getValue(String::class.java) ?: "Unknown"
                    val childID = childSnapshot.child("childID").getValue(String::class.java) ?: "N/A"
                    val date = childSnapshot.child("date").getValue(String::class.java) ?: "Unknown"
                    val latitude = childSnapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                    val longitude = childSnapshot.child("longitude").getValue(Double::class.java) ?: 0.0
                    val parentID = childSnapshot.child("parentID").getValue(String::class.java) ?: "N/A"
                    val time = childSnapshot.child("time").getValue(String::class.java) ?: "Unknown"

                    // Convert latitude and longitude to a string
                    val coordinates = "$latitude, $longitude"

                    // Log the fetched data for debugging
                    Log.d("DeviceFragment", "Battery: $battery")
                    Log.d("DeviceFragment", "ChildID: $childID")
                    Log.d("DeviceFragment", "ParentID: $parentID")

                    Log.d("DeviceFragment", "Date: $date")
                    Log.d("DeviceFragment", "Coordinates: $coordinates")
                    Log.d("DeviceFragment", "Time: $time")

                    // Add the data to the list
                    deviceList.add(
                        DeviceAdapter.ItemData(
                            circleText = parentID.firstOrNull()?.toString() ?: "", // First char of parentID
                            title = "No", // Use childID as the title
                            connectedNow = "", // Show battery info as connected status
                            date = time, // Combine date and time
                            lastMap = "Gilgit", // Use combined latitude and longitude string
                            battery = battery,
                            latLong = LatLng(latitude,longitude),
                            childId = childID,
                            parentId = parentID

                        )
                    )
                }

                // Set the data to the adapter
                deviceAdapter = DeviceAdapter(deviceList)
                recyclerView.adapter = deviceAdapter
            } else {
                showNoDataDialog()
            }
        }.addOnFailureListener { exception ->
            // Handle error
            exception.printStackTrace()
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
        dialogBuilder.setTitle("No Data Available")
        dialogBuilder.setMessage("No device connections found. Please check your connection or try again later.")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

}
