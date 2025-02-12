package com.example.locationtracker

import Checker
import HomeFragment
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment

import com.example.locationtracker.databinding.ActivityHomeBinding
import com.example.locationtracker.fragments.ConnectFragment
import com.example.locationtracker.fragments.DeviceFragment
import com.example.locationtracker.fragments.ProfileFragment
import com.example.permissionmanager.PermissionsManager



class HomeActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var checker: Checker
    private var internetDialog: AlertDialog? = null
    private var gpsDialog: AlertDialog? = null
    private lateinit var connectivityReceiver: ConnectivityReceiver
    private lateinit var permissionsManager: PermissionsManager

    companion object {
        private const val REQUEST_CODE_INTERNET_SETTINGS = 1001
        private const val REQUEST_CODE_GPS_SETTINGS = 1002
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(binding.root)

        checker = Checker(this)
        connectivityReceiver = ConnectivityReceiver(this)
        permissionsManager = PermissionsManager(this)




        binding.bottomNavigationView.itemRippleColor = null

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                R.id.nav_devices -> {
                    loadFragment(DeviceFragment())
                    true
                }

                R.id.nav_connect -> {
                    loadFragment(ConnectFragment())
                    true
                }

                else -> false
            }
        }


        // Load the default fragment (e.g., HomeFragment)
        loadFragment(HomeFragment())
    }

    @SuppressLint("HardwareIds")
    override fun onResume() {
        super.onResume()




        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        checkAndRequestPermissions()
        checkAndRequestGPS()

        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)




    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityReceiver)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment) // Use the ID of the FrameLayout
            .commit()
    }

    private fun checkAndRequestPermissions() {
        if (!checker.isInternetEnabled()) {
            showInternetDisabledDialog()
        }


    }

    private fun checkAndRequestGPS() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledDialog()
        }
    }

    private fun showInternetDisabledDialog() {
        if (internetDialog?.isShowing == true) {
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_generic_disabled, null)

        internetDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set up dialog views
        dialogView.findViewById<ImageView>(R.id.dialog_icon).setImageResource(R.drawable.logo2)
        dialogView.findViewById<TextView>(R.id.dialog_title).text =
            getString(R.string.internet_disabled)
        dialogView.findViewById<TextView>(R.id.dialog_message).text =
            getString(R.string.internet_connection_is_required_for_this_app_to_function_properly_please_enable_internet_connectivity)

        // Handle button clicks
        dialogView.findViewById<AppCompatButton>(R.id.btn_settings).setOnClickListener {
            startActivityForResult(
                Intent(Settings.ACTION_WIRELESS_SETTINGS),
                REQUEST_CODE_INTERNET_SETTINGS
            )
            internetDialog?.dismiss()
        }
        dialogView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
            internetDialog?.dismiss()
        }

        internetDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        internetDialog?.show()
    }

    private fun showGPSDisabledDialog() {
        if (gpsDialog?.isShowing == true) {
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_generic_disabled, null)

        gpsDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set up dialog views for GPS
        dialogView.findViewById<ImageView>(R.id.dialog_icon).setImageResource(R.drawable.logo2)
        dialogView.findViewById<TextView>(R.id.dialog_title).text =
            getString(R.string.allow_location_access)
        dialogView.findViewById<TextView>(R.id.dialog_message).text =
            getString(R.string.location_is_required_for_this_app_to_function_properly_please_enable_location_services)

        // Handle button clicks
        dialogView.findViewById<AppCompatButton>(R.id.btn_settings).setOnClickListener {
            startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                REQUEST_CODE_GPS_SETTINGS
            )
            gpsDialog?.dismiss()
        }
        dialogView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
            gpsDialog?.dismiss()
        }

        gpsDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        gpsDialog?.show()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            showInternetDisabledDialog()
        } else {
            internetDialog?.dismiss()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API...")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_INTERNET_SETTINGS) {
            if (checker.isInternetEnabled()) {
                internetDialog?.dismiss()
            } else {
                showInternetDisabledDialog()
            }
        }

        if (requestCode == REQUEST_CODE_GPS_SETTINGS) {
            checkAndRequestGPS()  // Recheck GPS status after returning from settings
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.handleMotionPermissionResult(requestCode, grantResults)
    }
}








