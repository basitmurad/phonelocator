
package com.example.locationtracker.fragments

import DeviceAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R

class DeviceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_device, container, false)

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recuycelr)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Create dummy data
        val dummyData = listOf(
            DeviceAdapter.ItemData(
                circleText = "A",
                title = "TECNO-KJ5 parents",
                connectedNow = "Yes",
                date = "2024-12-26",
                lastMap = "Map 1",
                location = "New York",
            ),
            DeviceAdapter.ItemData(
                circleText = "B",
                title = "Device 2",
                connectedNow = "No",
                date = "2024-12-25",
                lastMap = "Map 2",
                location = "Los Angeles",
            ),
            DeviceAdapter.ItemData(
                circleText = "C",
                title = "Device 3",
                connectedNow = "Yes",
                date = "2024-12-24",
                lastMap = "Map 3",
                location = "Chicago",
            )
        )

        // Set up the adapter with the dummy data
        deviceAdapter = DeviceAdapter(dummyData)
        recyclerView.adapter = deviceAdapter

        return view
    }
}
