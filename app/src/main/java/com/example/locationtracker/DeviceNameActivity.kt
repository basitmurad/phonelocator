package com.example.locationtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.databinding.ActivityDeviceNameBinding

class DeviceNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding object
        binding = ActivityDeviceNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default text to TextView

    }

    fun backButton(view: View) {

        finish()

    }

    fun navigateToNextScreen(view: View) {


        val intent = Intent(this, PermissionActivity::class.java)
        startActivity(intent)

    }
}
