package com.example.locationtracker.models



data class ConnectionRequest(
    val deviceId: String,
    val connectionId: String
)

data class ConnectionResponse(
    val success: Boolean,
    val message: String,
    val connection: ConnectionDetails
)

data class ConnectionDetails(
    val deviceId: String,
    val connectionId: String,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class QRCodeResponse(
    val success: Boolean,
    val qrCode: String,
    val message: String
)

data class ConnectionDetailsResponse(
    val _id: String,
    val deviceId: String,
    val connectionId: ConnectionIdDetails
)

data class ConnectionIdDetails(
    val _id: String,
    val deviceId: String,
    val deviceName: String,
    val image: String
)
