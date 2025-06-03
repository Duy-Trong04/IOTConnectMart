package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Review
import com.example.ungdungbanthietbi_iot.models.Reviews
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

data class ReviewResponse(
    val status_code: Int,
    val data: ReviewData
)
data class ReviewData(
    val data: List<Reviews>,
    val total_page: Int
)


interface ReviewAPIService {
    @GET ("review/product/{id}")
    suspend fun getReviewByIdDevice(
        @Path("id") id: String
    ): ReviewResponse

    @GET ("review_device/show.php")
    suspend fun getReviewByIdReview(
        @Query("id") id: Int
    ): Review

    @GET ("review/")
    suspend fun getAllReviews(): ReviewResponse

    @POST("review_device/create.php")
    suspend fun addReview(
        @Body review: Review
    ): AddReviewResponse

    @PUT("review_device/update.php")
    suspend fun updateReview(
        @Body review: Review
    ): AddReviewResponse
}