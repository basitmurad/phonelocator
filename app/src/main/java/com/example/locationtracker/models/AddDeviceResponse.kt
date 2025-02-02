package com.example.locationtracker.models

// Data class for API response
data class AddDeviceResponse(
    val success: Boolean,
    val message: String
)

// Data class for the device information
data class AddDeviceRequest(
    val deviceId: String,
    val deviceName: String
)
