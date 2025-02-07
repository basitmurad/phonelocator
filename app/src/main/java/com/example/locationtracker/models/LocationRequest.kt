package com.example.locationtracker.models

data class LocationRequest(
    val deviceId: String,
    val lat: String,
    val lng: String,
    val batteryPercentage: Int,
    val date: String,
    val time: String
)

data class LocationResponse(
    val success: Boolean,
    val message: String,
    val location: LocationData
)

data class LocationData(
    val _id: String,
    val deviceId: String,
    val lat: String,
    val lng: String,
    val batteryPercentage: Int,
    val date: String,
    val time: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class LocationHistoryResponse(
    val success: Boolean,
    val message: String,
    val locations: List<LocationData>
)

data class RecentLocationResponse(
    val success: Boolean,
    val message: String,
    val locations: List<LocationData>
)

//data class LocationResponse(
//    val success: Boolean,
//    val message: String,
//    val location: Location
//)

data class Location(
    val _id: String,
    val deviceId: String,
    val lat: String,
    val lng: String,
    val batteryPercentage: Int,
    val date: String,
    val time: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)





