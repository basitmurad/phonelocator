package com.example.locationtracker.models

data class UpdateDeviceProfileRequest(
    val deviceName: String?,
    val picture: String?
)

data class DeviceProfileResponse1(
    val success: Boolean,
    val message: String,
    val profile: Profile
)

data class Profile1(
    val _id: String,
    val deviceId: String,
    val deviceName: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val image: String
)
