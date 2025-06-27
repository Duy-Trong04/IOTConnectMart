package com.example.ungdungbanthietbi_iot.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


// Data class cho sản phẩm yêu thích
data class LikedProduct(
    val id: Int,
    val product_id: String,
    val name: String,
    val image: String?,
    val selling_price: Int,
    val description: String,
    val created_at: String,
    val updated_at: String?,
    val deleted_at: String?
)

// Data class cho phản hồi danh sách yêu thích
data class LikedResponse(
    val status_code: Int,
    val data: LikedData
)

data class LikedData(
    val data: List<LikedProduct>,
    val total_page: Int
)

// Data class cho yêu cầu thêm yêu thích
data class AddLikedRequest(
    val customer_id: String,
    val product_id: String
)

// Data class cho phản hồi thêm yêu thích
data class AddLikedResponse(
    val status_code: Int,
    val data: LikedItem
)

data class LikedItem(
    val id: Int,
    val customer_id: String,
    val product_id: String,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?
)

// Data class cho phản hồi xóa yêu thích
data class DeleteLikedResponse(
    val status_code: Int,
    val data: String
)

interface LikedAPIService {

    @GET("liked/detail/{customer_id}")
    suspend fun getLikedProducts(@Path("customer_id") customer_id: String): LikedResponse

    @POST("liked/")
    suspend fun addLikedProduct(@Body request: AddLikedRequest): AddLikedResponse

    @DELETE("liked/{customer_id}/{product_id}")
    suspend fun deleteLikedProduct(
        @Path("customer_id") customer_id: String,
        @Path("product_id") product_id: String
    ): DeleteLikedResponse
}