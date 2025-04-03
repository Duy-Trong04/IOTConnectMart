package com.example.ungdungbanthietbi_iot.data.review_device

import com.example.ungdungbanthietbi_iot.data.liked.Liked
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class AddReviewResponse(
    val success: Boolean,
    val message: String
)

interface ReviewAPIService {
    @GET ("review_device/getReviewByIdDevice.php")
    suspend fun getReviewByIdDevice(
        @Query("idDevice") idDevice: String
    ): List<Review>

    @GET ("review_device/getReviewByIdCustomer.php")
    suspend fun getReviewByIdCustomer(
        @Query("idCustomer") idCustomer: String,
        @Query("status") status: Int,
    ): List<Review>

    @POST("review_device/create.php")
    suspend fun addReview(
        @Body review: Review
    ): AddReviewResponse

    @PUT("review_device/update.php")
    suspend fun updateReview(
        @Body review: Review
    ): AddReviewResponse
}