package com.example.locationtracker.api
import com.example.locationtracker.models.AddDeviceRequest
import com.example.locationtracker.models.AddDeviceResponse
import com.example.locationtracker.models.ConnectionRequest
import com.example.locationtracker.models.ConnectionResponse
import com.example.locationtracker.models.DeviceProfileResponse
import com.example.locationtracker.models.QRCodeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {

    @POST("/devices/add")
    fun addDevice(@Body device: AddDeviceRequest): Call<AddDeviceResponse>

    @GET("/devices/profile/{deviceId}")
    fun getDeviceProfile(@Path("deviceId") deviceId: String): Call<DeviceProfileResponse>

    @Multipart
    @PUT("/devices/update/{deviceId}")
    fun updateDevice(
        @Path("deviceId") deviceId: String,
        @Part("deviceName") deviceName: RequestBody?,
        @Part image: MultipartBody.Part? // the image will be sent as a binary file
    ): Call<DeviceProfileResponse>





    @POST("/connections/create")
    fun createConnection(@Body requestBody: ConnectionRequest): Call<ConnectionResponse>

    @GET("/connections/qrcode/{deviceId}")
    fun generateQRCode(@Path("deviceId") deviceId: String): Call<QRCodeResponse>






}
