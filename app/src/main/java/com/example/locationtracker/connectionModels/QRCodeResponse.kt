package com.example.locationtracker.connectionModels

import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.models.ConnectionRequest
import com.example.locationtracker.models.ConnectionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

import retrofit2.http.GET
import retrofit2.http.Path

data class QRCodeResponse(
    val success: Boolean,
    val qrCode: String,  // This will hold the base64 image string of the QR code
    val message: String
)

//private fun createConnection(deviceId: String, connectionId: String) {
//    // Prepare the request body
//    val connectionRequest = ConnectionRequest(deviceId, connectionId)
//
//    // Call the API
//    RetrofitClient.apiService.createConnection(connectionRequest)
//        .enqueue(object : Callback<ConnectionResponse> {
//            override fun onResponse(
//                call: Call<ConnectionResponse>,
//                response: Response<ConnectionResponse>
//            ) {
//                if (response.isSuccessful) {
//                    // Handle success
//                    val connectionResponse = response.body()
//                    if (connectionResponse != null && connectionResponse.success) {
//                        // Successfully created the connection
//                        println("Connection Created: ${connectionResponse.message}")
//                        println("Connection ID: ${connectionResponse.connection?._id}")
//                    } else {
//                        // Handle the case where the connection wasn't created
//                        println("Error: ${connectionResponse?.message}")
//                    }
//                } else {
//                    // Handle unsuccessful response
//                    println("Error: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ConnectionResponse>, t: Throwable) {
//                // Handle failure
//                println("Failed to create connection: ${t.message}")
//            }
//        })
//}


