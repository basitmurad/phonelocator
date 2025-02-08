package com.example.locationtracker.models

data class LocationRequest(
    val deviceId: String,
    val lat: String,
    val lng: String,
    val batteryPercentage: Int,
    val date: String,
    val time: String
)

// Response model for storing location
data class LocationResponse(
    val success: Boolean,
    val message: String,
    val location: LocationData?
)

// Model for location data
data class LocationData(
    val _id: String,
    val deviceId: String,
    val lat: String,
    val lng: String,
    val batteryPercentage: Int,
    val date: String,
    val time: String,
    val createdAt: String,
    val updatedAt: String
)
data class LocationHistoryResponse(
    val success: Boolean,
    val message: String,
    val locations: List<LocationData>?
)