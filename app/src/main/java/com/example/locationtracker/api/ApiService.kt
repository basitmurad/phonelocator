package com.example.locationtracker.api
import com.example.locationtracker.models.AddDeviceRequest
import com.example.locationtracker.models.AddDeviceResponse
import com.example.locationtracker.models.ConnectionDetails
import com.example.locationtracker.models.ConnectionRequest
import com.example.locationtracker.models.ConnectionResponse
import com.example.locationtracker.models.DeviceProfileResponse
import com.example.locationtracker.models.LocationHistoryResponse
import com.example.locationtracker.models.LocationRequest
import com.example.locationtracker.models.LocationResponse
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

//    @Multipart
//    @PUT("/devices/update/{deviceId}")
//    fun updateDevice(
//        @Path("deviceId") deviceId: String,
//        @Part("deviceName") deviceName: RequestBody?,
//        @Part image: MultipartBody.Part? // the image will be sent as a binary file
//    ): Call<DeviceProfileResponse>


    @Multipart
    @PUT("devices/update/{deviceId}")
    fun updateDevice(
        @Path("deviceId") deviceId: String,
        @Part("deviceName") deviceName: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<DeviceProfileResponse>




    @POST("/connections/create")
    fun createConnection(@Body requestBody: ConnectionRequest): Call<ConnectionResponse>

    @GET("/connections/qrcode/{deviceId}")
    fun generateQRCode(@Path("deviceId") deviceId: String): Call<QRCodeResponse>



    @GET("connections/all/{deviceId}")
    fun getConnections(@Path("deviceId") deviceId: String): Call<List<ConnectionDetails>>




    @POST("locations/add")
    fun addLocation(@Body locationRequest: LocationRequest): Call<LocationResponse>

    // Get location history
    @GET("locations/history/{connectionId}")
    fun getLocationHistory(@Path("connectionId") connectionId: String): Call<LocationHistoryResponse>

    // Get recent locations
    @GET("locations/recent/{connectionId}")
    fun getRecentLocations(@Path("connectionId") connectionId: String): Call<LocationHistoryResponse>



}
