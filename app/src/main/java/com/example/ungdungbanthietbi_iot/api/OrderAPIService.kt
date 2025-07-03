package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.CheckoutRequest
import com.example.ungdungbanthietbi_iot.models.Order
import com.example.ungdungbanthietbi_iot.models.ProductError
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class OrderResponse(
    val status_code: Int,
    val data: OrderData,
)
data class OrderData(
    val data: List<Order>,
    val total_page: Int
)

data class CheckoutResponse(
    val status_code: Int,
    val error_code: Int? = null,
    val data: OrderDataCheckOut, // Adjust type based on success data
    val data_errors: List<ProductError>? = null
)
data class OrderRequestCancel(val order_id: String)

data class OrderDataCheckOut(
    @SerializedName("order_id") val orderId: String,
    @SerializedName("total_money") val totalMoney: Int,
    val status: Int,
    @SerializedName("created_at") val createdAt: String?
)

data class OrderFinishedRequest(
    val order_id: String,
    val customer_id: String
)

interface OrderAPIService {
    @POST("order/checkout")
    suspend fun createOrder(@Body request: CheckoutRequest): CheckoutResponse

    @GET("order/customer/{customerId}")
    suspend fun getOrdersByCustomer(@Path("customerId") customerId: String): OrderResponse

    @PATCH("order/finished")
    suspend fun finishedOrder(
        @Body request: OrderFinishedRequest
    ): Response<Unit>

    @PUT("order/customer")
    suspend fun cancelOrder(
        @Body request: OrderRequestCancel
    ): Response<Unit>
}