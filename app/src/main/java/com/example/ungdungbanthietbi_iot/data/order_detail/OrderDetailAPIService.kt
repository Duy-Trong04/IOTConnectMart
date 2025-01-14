package com.example.ungdungbanthietbi_iot.data.order_detail

import com.example.ungdungbanthietbi_iot.data.order.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class addOrderDetailResponse(
    val success: Boolean,
    val message: String
)

interface OrderDetailAPIService {
    @POST("order_detail/create.php")
    suspend fun addOrderDetail(
        @Body orderDetail: OrderDetail
    ): addOrderDetailResponse

    @GET("order_detail/getHoaDonBanByKhachHang.php")
    suspend fun getOrderDetailByCustomer(
        @Query("idCustomer") idCustomer: String,
        @Query("status") status: Int
    ): OrderResponse
}