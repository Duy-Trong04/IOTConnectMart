package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Review
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class AddReviewResponse(
    val success: Boolean,
    val message: String
)

data class getReviewResponse(
    val success: Boolean,
    val review: Review?
)

data class CheckReviewResponse(
    val success: Boolean,
    val review_exists: Boolean
)
interface ReviewAPIService {

    @GET ("review_device/getReview.php")
    suspend fun checkReview(
        @Query("idCustomer") idCustomer: String,
        @Query("idDevice") idDevice: Int,
        @Query("status") status: Int
    ): getReviewResponse

    @GET ("review_device/checkReview.php")
    suspend fun checkReview2(
        @Query("idCustomer") idCustomer: String,
        @Query("idDevice") idDevice: Int,
        @Query("status") status: Int
    ): CheckReviewResponse

    @GET ("review_device/getReviewByIdDevice.php")
    suspend fun getReviewByIdDevice(
        @Query("idDevice") idDevice: String
    ): List<Review>

    @GET ("review_device/show.php")
    suspend fun getReviewByIdReview(
        @Query("id") id: Int
    ): Review

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