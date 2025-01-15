package com.example.ungdungbanthietbi_iot.data.liked

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LikedResponse(
    val liked: List<Liked>
)
data class UpdateResponse(
    val success: Boolean,
    val message: String
)

data class ApiResponse(
    val message: String
)

data class DeleteRequest(
    val id: Int
)

data class DeleteidDeviceResponse(
    val idCustomer: String,
    val idDevice:Int
)

data class AddLikedResponse(
    val success: Boolean,
    val message: String
)
interface LikedAPIService {
    @GET("liked/getLikedByIdCustomer.php")
    suspend fun getLikedByIdCustomer(
        @Query("idCustomer") idCustomer: String
    ): LikedResponse

    @PUT("liked/update.php")
    suspend fun updateLiked(
        @Body liked: Liked
    ):UpdateResponse

    @POST("liked/delete.php")
    suspend fun deleteLiked(
        @Body deleteRequest: DeleteRequest
    ): Response<ApiResponse>

    @POST("liked/deleteLikedByCustomer.php")
    suspend fun deleteLikedByCustomer(
        @Body deleteRequest: DeleteidDeviceResponse
    ): Response<ApiResponse>

    @POST("liked/create.php")
    suspend fun addliked(
        @Body liked: Liked
    ): AddLikedResponse
}