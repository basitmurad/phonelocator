package com.example.locationtracker.models

import java.io.File

data class UpdateDeviceRequest(
    val deviceName: String? = null, // Optional for updating name
    val image: File? = null         // Use File object for image
)
