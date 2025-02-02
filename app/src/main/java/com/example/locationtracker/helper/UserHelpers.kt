package com.example.locationtracker.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserHelpers(private val context: Context) {


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss") // Format for time
        return current.format(timeFormatter) ?: "Unknown Time"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Format for date
        return current.format(dateFormatter) ?: "Unknown Date"
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return try {
            context.contentResolver.let {
                android.provider.Settings.Secure.getString(it, android.provider.Settings.Secure.ANDROID_ID) ?: "Unknown Android ID"
            }
        } catch (e: Exception) {
            "Unknown Android ID"
        }
    }




}

