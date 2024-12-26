
package com.example.locationtracker

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.databinding.ActivityDeviceNameBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class DeviceNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceNameBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var deviceReference: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    // Separate variables for device details
    private lateinit var manufacturer: String
    private lateinit var model: String
    private lateinit var androidVersion: String
    private var sdkVersion: Int = 0
    private lateinit var deviceName: String
    private lateinit var androidId: String
    private lateinit var uniqueCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding object
        binding = ActivityDeviceNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        deviceReference = database.getReference("devices")

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating Device Details")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)

        // Fetch device details and store them in separate variables
        fetchDeviceDetails()

        // Set default device name in the EditText
        binding.textView.text = Editable.Factory.getInstance().newEditable(deviceName)

        // Set button click listener to update data
        binding.appCompatButton.setOnClickListener {
            updateDeviceNameInFirebase()
        }
    }

    private fun fetchDeviceDetails() {
        manufacturer = Build.MANUFACTURER
        model = Build.MODEL
        androidVersion = Build.VERSION.RELEASE
        sdkVersion = Build.VERSION.SDK_INT
        deviceName = "${Build.DEVICE} (${Build.BRAND})"
        androidId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        uniqueCode = generateUniqueCode() // Generate the unique code
    }

    private fun updateDeviceNameInFirebase() {
        // Show progress dialog
        progressDialog.show()

        // Get the updated device name from the EditText
        val updatedDeviceName = binding.textView.text.toString()

        // Create a map to store device details
        val deviceDetails = mapOf(
            "deviceName" to updatedDeviceName,
            "manufacturer" to manufacturer,
            "model" to model,
            "androidVersion" to androidVersion,
            "sdkVersion" to sdkVersion,
            "uniqueCode" to uniqueCode // Include the unique code
        )

        // Save data under the unique device ID in Firebase
        deviceReference.child(androidId).setValue(deviceDetails)
            .addOnCompleteListener { task ->
                // Dismiss the progress dialog
                progressDialog.dismiss()

                if (task.isSuccessful) {
                    val intent = Intent(this, PermissionActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Device details updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update device details.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun generateUniqueCode(): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
        return (1..6)
            .map { characters[Random.nextInt(characters.length)] }
            .joinToString("")
    }
}
