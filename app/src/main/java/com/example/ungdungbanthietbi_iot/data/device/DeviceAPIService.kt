package com.example.ungdungbanthietbi_iot.data.device

import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DeviceAPIService {
    @GET ("device/read.php")
    suspend fun getAllDevice(): List<Device>

    @GET ("device/getDevicePriceThan5M.php")
    suspend fun getDeviceFeatured(): List<Device>

    @GET("device/show.php")
    suspend fun getDeviceById(@Query("id") id: String): Device
}