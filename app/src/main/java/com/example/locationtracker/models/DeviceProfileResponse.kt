package com.example.locationtracker.models


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
