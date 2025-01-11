package com.example.ungdungbanthietbi_iot.data.review_device

import retrofit2.http.GET
import retrofit2.http.Query

interface ReviewAPIService {
    @GET ("review_device/getReviewByIdDevice.php")
    suspend fun getReviewByIdDevice(
        @Query("idDevice") idDevice: String
    ): List<Review>
}