package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.OrderDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class addOrderDetailResponse(
    val success: Boolean,
    val message: String
)
data class OrderDetailResponse(
    val order: List<OrderDetail>
)
interface OrderDetailAPIService {
    @POST("order_detail/create.php")
    suspend fun addOrderDetail(
        @Body orderDetail: OrderDetail
    ): addOrderDetailResponse

    @GET("order_detail/getOrderDetailByIdOrder.php")
    suspend fun getOrderDetailByIdOrder(
        @Query("idOrder") idOrder: Int,
    ): OrderDetailResponse

    @GET("order_detail/getOrderDetailByStatus.php")
    suspend fun getOrderDetailByStatus(
        @Query("idCustomer") idCustomer: String,
        @Query("status") status: Int
    ): OrderDetailResponse
}