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

data class DeviceProfileResponse(
    val success: Boolean,
    val message: String,
    val profile: Profile
)

data class Profile(
    val _id: String,
    val deviceId: String,
    val deviceName: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val image:String
)
