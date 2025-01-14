package com.example.ungdungbanthietbi_iot.data.order

import com.example.ungdungbanthietbi_iot.data.cart.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class addOrderResponse(
    val success: Boolean,
    val message: String
)

data class OrderResponse(
    val order: List<Order>? // Có thể API trả về null nếu không có dữ liệu
)


data class idOrderResponse(
    val id: Int
)

data class orderDeleteRequest(
    val id: Int
)



interface OrderAPIService {
    @POST("order/create.php")
    suspend fun addOrder(
        @Body order: Order
    ): addOrderResponse

    @GET("order/getOrderByCustomer.php")
    suspend fun getOrderByCustomer(
        @Query("idCustomer") idCustomer: String,
        @Query("status") status: Int
    ): OrderResponse

    @POST("order/delete.php")
    suspend fun deleteOrder(
        @Body deleteRequest: orderDeleteRequest
    ): Response<ApiResponse>

    @PUT("order/update.php")
    suspend fun updateOrder(
        @Body order: Order
    ): addOrderResponse
}