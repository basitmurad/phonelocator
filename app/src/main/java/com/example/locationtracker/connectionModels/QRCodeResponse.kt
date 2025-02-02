package com.example.locationtracker.connectionModels

import com.example.locationtracker.models.ConnectionRequest
import com.example.locationtracker.models.ConnectionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

import retrofit2.http.GET
import retrofit2.http.Path

data class QRCodeResponse(
    val success: Boolean,
    val qrCode: String,  // This will hold the base64 image string of the QR code
    val message: String
)

interface ApiService {

}

