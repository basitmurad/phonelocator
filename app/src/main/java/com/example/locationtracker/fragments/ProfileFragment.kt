package com.example.locationtracker.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.locationtracker.R
import com.example.locationtracker.screens.PrivacyPolicyActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var buttonOpen: LinearLayout
    private lateinit var buttonRateUs: LinearLayout
    private lateinit var btnFeedback: LinearLayout
    private lateinit var btnShare: LinearLayout
    private lateinit var buttonChangeName: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var deviceReference: DatabaseReference
    private lateinit var androidId: String
    private lateinit var deviceName: String
    private lateinit var textname:TextView
    private lateinit var name:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        buttonOpen = view.findViewById(R.id.btnPrivacy)
        buttonRateUs = view.findViewById(R.id.btnRateUs)
        buttonChangeName = view.findViewById(R.id.profileName)
        btnShare = view.findViewById(R.id.btnShareApp)
        textname = view.findViewById(R.id.textname)
        btnFeedback = view.findViewById(R.id.btnFeedback)
        database = FirebaseDatabase.getInstance()
        deviceReference = database.getReference("devices")

        androidId = getAndroidId()

        fetchDeviceInformation()


        // Set the click listener to open the privacy screen
        btnFeedback.setOnClickListener {
            goNextScreen()

        }
        btnShare.setOnClickListener {
            val apkFile = File(requireContext().filesDir, "Location Tracker.apk")

            // Create an intent to share the APK file
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/vnd.android.package-archive"
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile))

            // Show share options
            startActivity(Intent.createChooser(intent, "Share app via"))
        }
        btnShare.setOnClickListener {
            shareCode()
        }
        buttonChangeName.setOnClickListener {
            showDeviceName(deviceName)
        }

        // Set the click listener to show the rating dialog
        buttonRateUs.setOnClickListener {
            showRatingDialog()
        }

        return view
    }

    private fun goNextScreen() {
        // Navigate to PrivacyPolicyActivity
        val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
        startActivity(intent)
    }

    private fun showRatingDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rating, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Ensuring the dialog background is transparent
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Show the dialog
        alertDialog.show()

    }

    @SuppressLint("MissingInflatedId")
    private fun showDeviceName(deviceName: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.device_name_layout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Ensuring the dialog background is transparent
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Show the dialog
        alertDialog.show()

        val displayNameDi: TextView = dialogView.findViewById(R.id.displayNameDi)
        val displayNameEdit: EditText = dialogView.findViewById(R.id.displayNameEdit)
        val displayNameButton: Button = dialogView.findViewById(R.id.displayNameButton)
        val displayNameCancel: Button = dialogView.findViewById(R.id.displayNameCancel)

        // Set initial device name
        displayNameDi.text = deviceName

        // Set button listeners
        displayNameButton.setOnClickListener {
            val newDeviceName = displayNameEdit.text.toString().trim()
            if (newDeviceName.isNotEmpty()) {
                updateDeviceNameInDatabase(newDeviceName)
                alertDialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        displayNameCancel.setOnClickListener {
            alertDialog.dismiss()
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

    private fun updateDeviceNameInDatabase(newName: String) {
        deviceReference.child(androidId).child("deviceName").setValue(newName)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Device name updated successfully!", Toast.LENGTH_SHORT).show()
                buttonChangeName.text = newName // Update the UI
                textname.text = newName.firstOrNull()?.toString() ?: ""
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Failed to update device name: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

//    @SuppressLint("MissingInflatedId")
//    private fun showDeviceName(deviceName: String) {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.device_name_layout, null)
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setCancelable(true)
//
//
//
//        val alertDialog = dialogBuilder.create()
//
//        // Ensuring the dialog background is transparent
//        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//        // Show the dialog
//        alertDialog.show()
//        val displayNameDi: TextView = dialogView.findViewById(R.id.displayNameDi)
//        val displayNameEdit: TextView = dialogView.findViewById(R.id.displayNameEdit)
//        val displayNameButton: TextView = dialogView.findViewById(R.id.displayNameButton)
//        val displayNameCancel: TextView = dialogView.findViewById(R.id.displayNameCancel)
//        displayNameDi.text = deviceName
//
//
//        val window = alertDialog.window
//        if (window != null) {
//            val layoutParams = window.attributes
//            val margin = 50 // Set margin in pixels (e.g., 50px)
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            window.setLayout(
//                (resources.displayMetrics.widthPixels - 2 * margin),
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            window.attributes = layoutParams
//        }
//
//    }

    private fun shareCode() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Download the app from :")
        }

        // Show the chooser dialog for sharing options
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return requireContext().contentResolver.let {
            android.provider.Settings.Secure.getString(it, android.provider.Settings.Secure.ANDROID_ID)
        }
    }
    private fun fetchDeviceInformation() {

        deviceReference.child(androidId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    // Get the device name
                    deviceName = snapshot.child("deviceName").getValue(String::class.java) ?: "N/A"

                    buttonChangeName.text = deviceName
                    // Extract the first character of the device name
                    val firstChar = deviceName.firstOrNull()?.toString() ?: ""

                    // Set the first character to the textname TextView

                    textname.text= firstChar

                    println("device name is $deviceName and first char is $firstChar")
                    val manufacturer = snapshot.child("manufacturer").getValue(String::class.java) ?: "N/A"
                    val model = snapshot.child("model").getValue(String::class.java) ?: "N/A"
                    val androidVersion = snapshot.child("androidVersion").getValue(String::class.java) ?: "N/A"
                    val sdkVersion = snapshot.child("sdkVersion").getValue(Int::class.java) ?: -1
                    val uniqueCode = snapshot.child("uniqueCode").getValue(String::class.java) ?: "N/A"

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
