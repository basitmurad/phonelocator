//package com.example.locationtracker.models
//
//
//
//data class ConnectionRequest(
//    val deviceId: String,
//    val connectionId: String
//)
//
//data class ConnectionResponse(
//    val success: Boolean,
//    val message: String,
//    val connection: ConnectionDetails
//)
//
//data class ConnectionDetails(
//    val deviceId: String,
//    val connectionId: String,
//    val _id: String,
//    val createdAt: String,
//    val updatedAt: String,
//    val __v: Int
//)
//
//data class QRCodeResponse(
//    val success: Boolean,
//    val qrCode: String,  // This will hold the base64 image string of the QR code
//    val message: String
//)
//
//
//
//
//
package com.example.locationtracker.models

data class ConnectionRequest(
    val deviceId: String,
    val connectionId: String
)

data class ConnectionResponse(
    val success: Boolean,
    val message: String,
    val connections: List<ConnectionDetails> // Change from single to List
)

data class ConnectionDetails(
    val _id: String,
    val deviceId: String,
    val connectionId: ConnectedDevice,  // ✅ Change from String to an Object
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class ConnectedDevice(  // ✅ New class for connectionId object
    val _id: String,
    val deviceId: String,
    val deviceName: String,
    val image: String
)


//data class ConnectionDetails(
//    val deviceId: String,
//    val connectionId: String,
//    val _id: String,
//    val createdAt: String,
//    val updatedAt: String,
//    val __v: Int
//)

data class QRCodeResponse(
    val success: Boolean,
    val qrCode: String,
    val message: String
)
