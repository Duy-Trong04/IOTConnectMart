package com.example.ungdungbanthietbi_iot.data.image_device

import retrofit2.http.GET
import retrofit2.http.Query

interface ImageAPIService {
    @GET("image_device/getImageDeviceByIdDevice.php")
    suspend fun getImageByIdDevice(
        @Query("idDevice") idDevice: String
    ): List<Image>
}