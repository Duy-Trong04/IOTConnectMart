package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Device
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("product/detail/{id}")
    suspend fun getDeviceById(
        @Path("id") id: String
    ): DeviceResponse

    @GET("product/")
    suspend fun getDeviceFeatured(
        @Query("limit") limit: Int
    ): DeviceResponse

    @GET("product/")
    suspend fun getDeviceSale(
        @Query("limit") limit: Int
    ): DeviceResponse

    @GET("product")
    suspend fun searchProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("filters") filters: String
    ): DeviceResponse
}