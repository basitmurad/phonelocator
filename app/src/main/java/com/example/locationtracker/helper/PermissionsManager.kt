package com.example.permissionmanager

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.annotation.RequiresApi

class PermissionsManager(private val activity: Activity) {

    // Constants
    companion object {
        private const val MOTION_PERMISSION_REQUEST_CODE = 101
    }

    // Motion Tracking Permissions
    @RequiresApi(Build.VERSION_CODES.Q)
    private val motionPermissions = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    /**
     * Request motion tracking permissions
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestMotionPermissions() {
        if (areMotionPermissionsGranted()) {
            Toast.makeText(activity, "Motion permissions already granted", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(activity, motionPermissions, MOTION_PERMISSION_REQUEST_CODE)
        }
    }

    /**
     * Check if motion tracking permissions are granted
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun areMotionPermissionsGranted(): Boolean {
        return motionPermissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Handle Motion Tracking Permissions Result
     */
    fun handleMotionPermissionResult(requestCode: Int, grantResults: IntArray): Boolean {
        if (requestCode == MOTION_PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(activity, "Motion permissions granted", Toast.LENGTH_SHORT).show()
                return true
            } else {
                Toast.makeText(activity, "Motion permissions denied", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return false
    }

    // Open Protected App Settings (Device-Specific)
    fun openProtectedAppSettings() {
        val intent = Intent().apply {
            component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
        }
        try {
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Cannot open Protected App settings. Please enable it manually.", Toast.LENGTH_LONG).show()
        }
    }

    // Open Auto-Start Settings (Device-Specific)
    fun openAutoStartSettings() {
        val intent = Intent().apply {
            component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
        }
        try {
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Cannot open Auto-Start settings. Please enable it manually.", Toast.LENGTH_LONG).show()
        }
    }
}
