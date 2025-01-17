package com.example.ungdungbanthietbi_iot.data.device

import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

data class DeviceResponse(
    val device: List<Device>
)

interface DeviceAPIService {
    @GET ("device/read.php")
    suspend fun getAllDevice(): List<Device>

    @GET ("device/getDevicePriceThan5M.php")
    suspend fun getDeviceFeatured(): List<Device>

    @GET("device/show.php")
    suspend fun getDeviceById(@Query("id") id: String): Device

    @GET("device/getDeviceByCart.php")
    suspend fun getDeviceByCart(
        @Query("idCustomer") idCustomer: String
    ): DeviceResponse

    @GET("device/getDeviceByLiked.php")
    suspend fun getDeviceByLiked(
        @Query("idCustomer") idCustomer: String
    ): DeviceResponse

    @GET("device/getDeviceByIdOrder.php")
    suspend fun getDeviceByIdOrder(
        @Query("id") id: Int
    ): DeviceResponse
}