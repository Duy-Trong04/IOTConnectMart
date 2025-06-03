package com.example.ungdungbanthietbi_iot.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path



// Data class cho sản phẩm yêu thích
data class LikedProduct(
    val id: Int,
    val name: String,
    val image: String?,
    val selling_price: Long,
    val description: String,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?
)

// Data class cho phản hồi danh sách yêu thích
data class LikedResponse1(
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
data class AddLikedResponse1(
    val status_code: Int,
    val data: LikedItem
)

data class LikedItem(
    val id: Int,
    val customer_id: String,
    val product_id: Int,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?
)

// Data class cho phản hồi xóa yêu thích
data class DeleteLikedResponse1(
    val status_code: Int,
    val data: String
)

data class Device(
    val idDevice: Int,
    val name: String,
    val image: String?,
    val sellingPrice: Long
)

interface LikedAPIService {

    @GET("liked/{customerId}")
    suspend fun getLikedProducts(@Path("customerId") customerId: String): LikedResponse1

    @POST("liked/")
    suspend fun addLikedProduct(@Body request: AddLikedRequest): AddLikedResponse1

    @DELETE("liked/{customerId}/{productId}")
    suspend fun deleteLikedProduct(
        @Path("customerId") customerId: String,
        @Path("productId") productId: String
    ): DeleteLikedResponse1
}