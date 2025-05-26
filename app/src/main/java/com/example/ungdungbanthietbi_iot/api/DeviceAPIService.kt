package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Device
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class DeviceResponse1(
    val device: List<Device>
)
data class DeviceResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("data") val data: DataWrapper
)

data class DataWrapper(
    @SerializedName("data") val data: List<Device>,
    @SerializedName("total_page") val totalPage: Int
)

interface DeviceAPIService {
    @GET ("product/")
    suspend fun getAllDevice(): DeviceResponse

    @GET ("device/getDevicePriceThan5M.php")
    suspend fun getDeviceFeatured(): List<Device>

    @GET("device/show.php")
    suspend fun getDeviceById1(
        @Path("id") id: String
    ): Device
    @GET("product/detail/{id}")
    suspend fun getDeviceById(
        @Path("id") id: Int
    ): DeviceResponse

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

    @GET("device/searchDevice.php")
    suspend fun searchDevice(
        @Query("name") name: String,
        @Query("des") des: String
    ): DeviceResponse
}