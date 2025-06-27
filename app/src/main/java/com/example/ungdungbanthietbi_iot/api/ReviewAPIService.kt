package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.ReviewDetail
import com.example.ungdungbanthietbi_iot.models.Reviews
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class ReviewResponse(
    val status_code: Int,
    val data: ReviewData
)
data class ReviewData(
    val data: List<Reviews>,
    val total_page: Int
)
data class ReviewRequestUpdate(
    val id: Int,
    val customer_id: String,
    val comment: String?,
    val image: String?,
    val rating: Int
)
data class ReviewResponseUpdate(
    val status_code: Int,
    val data: Reviews
)

data class ReviewDetailResponse(
    val status_code: Int,
    val data: ReviewDetail
)
data class ReviewRequestCreate(
    val customer_id: String,
    val product_id: String,
    val comment: String?,
    val image: String?,
    val rating: Int
)

interface ReviewAPIService {
    @GET ("review/product/{id}")
    suspend fun getReviewByIdDevice(
        @Path("id") id: String
    ): ReviewResponse

    @GET ("review/detail/{id}")
    suspend fun getReviewByIdReview(
        @Path("id") id: Int
    ): ReviewDetailResponse

    @GET ("review/")
    suspend fun getAllReviews(): ReviewResponse

    @POST("review/")
    suspend fun addReview(
        @Body request: ReviewRequestCreate
    ): ReviewResponseUpdate

    @PUT("review/")
    suspend fun updateReview(
        @Body request: ReviewRequestUpdate
    ): ReviewResponseUpdate
}