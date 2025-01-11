package com.example.ungdungbanthietbi_iot.data.device

import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DeviceAPIService {
    @GET ("device/admin")
    suspend fun getAllDevice(): List<Device>

    @GET ("device/")
    suspend fun getDeviceFeatured(): List<Device>

    @GET("device/detail/{slug}")
    suspend fun getDeviceBySlug(@Path("slug") slug: String): Device
}