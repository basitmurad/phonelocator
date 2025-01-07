
package com.example.locationtracker

import android.annotation.SuppressLint
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
import java.security.MessageDigest
import kotlin.random.Random

class DeviceNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceNameBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var deviceReference: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

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

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating Device Details")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)

        fetchDeviceDetails()

        binding.textView.text = Editable.Factory.getInstance().newEditable(deviceName)

        binding.appCompatButton.setOnClickListener {
            updateDeviceNameInFirebase()
        }
    }

    @SuppressLint("HardwareIds")
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
        uniqueCode = generateUniqueId() // Generate the unique code
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
            "uniqueCode" to uniqueCode, // Include the unique code
            "androidId" to androidId, // Include the unique code
        )

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

    @SuppressLint("HardwareIds")
    private fun generateUniqueId(): String {
        // Fetch the first 4 digits from androidId
        val firstFourDigits = androidId.take(4)

        // Fetch the last 4 characters from the model and remove any spaces
        val lastFourChars = model.take(4).replace(" ", "")

        // Combine both parts to generate the unique code
        val combinedString = firstFourDigits + lastFourChars

        // Convert the combined string into a mutable list of characters
        val shuffledList = combinedString.toList().shuffled(Random)

        // Convert the shuffled list back to a string
        val uniqueCode = shuffledList.joinToString("").toUpperCase()

        return uniqueCode
    }


}
