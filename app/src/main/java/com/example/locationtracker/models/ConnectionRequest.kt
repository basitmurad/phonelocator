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
    val qrCode: String,  // This will hold the base64 image string of the QR code
    val message: String
)





