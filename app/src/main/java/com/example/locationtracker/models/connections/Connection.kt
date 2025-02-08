package com.example.locationtracker.models.connections

// Define the data classes
data class Connection(
    val _id: String,
    val deviceId: String,
    val connectionId: ConnectionDetails,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class ConnectionDetails(
    val _id: String,
    val deviceId: String,
    val deviceName: String,
    val image: String
)