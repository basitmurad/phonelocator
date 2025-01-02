
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
        val connectionRef = database.child("Connections").child(androidId)

        // Retrieve the data from Firebase
        connectionRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Get the data from Firebase and populate the RecyclerView
                val deviceList = mutableListOf<DeviceAdapter.ItemData>()

                // Iterate through the children of the node
                snapshot.children.forEach { childSnapshot ->
                    val binding = childSnapshot.child("binding").getValue(Boolean::class.java) ?: false
                    val child = childSnapshot.child("child").getValue(String::class.java) ?: ""
                    val deviceName = childSnapshot.child("deviceName").getValue(String::class.java) ?: ""
                    val parent = childSnapshot.child("parent").getValue(String::class.java) ?: ""
                    val timestamp = childSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L

                    val formattedDate = convertTimestampToDate(timestamp)

                    // Log the fetched data for each device
                    Log.d("DeviceFragment", "Binding: $binding")
                    Log.d("DeviceFragment", "Child: $child")
                    Log.d("DeviceFragment", "Device Name: $deviceName")
                    Log.d("DeviceFragment", "Parent: $parent")
                    Log.d("DeviceFragment", "Timestamp: $timestamp")

                    // Add the data to the list
                    deviceList.add(
                        DeviceAdapter.ItemData(
                            circleText = deviceName.firstOrNull()?.toString() ?: "", // Set first character as circleText
                            title = deviceName,
                            connectedNow = if (binding) "Yes" else "No", // Assuming "binding" indicates if connected
                            date = formattedDate, // You can format the timestamp to a readable date if needed
                            lastMap = child, // Using the "child" as the lastMap for simplicity
                            location = parent // Using "parent" as the location for now
                        )
                    )
                }

                // Set the data to the adapter
                deviceAdapter = DeviceAdapter(deviceList)
                recyclerView.adapter = deviceAdapter
            }
            else{
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
