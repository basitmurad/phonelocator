// Data classes to represent request and response models

data class ConnectionRequest(
    val deviceId: String,
    val connectionId: String
)

data class ConnectRequest(
    val deviceId: String,
    val connectionId: String
)

data class ConnectionResponse(
    val success: Boolean,
    val message: String,
    val connection: ConnectionData?
)

data class ConnectionData(
    val deviceId: String,
    val connectionId: String,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
