package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Cart
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class CartResponse(
    val cart: List<Cart>
)
data class AddToCartResponse(
    val success: Boolean,
    val message: String
)
interface CartAPIService {
    @GET("cart/getCartByIdCustomer.php")
    suspend fun getCartByIdCustomer(
        @Query("idCustomer") idCustomer: String
    ): CartResponse

    @PUT("cart/update.php")
    suspend fun updateCart(
        @Body cart: Cart
    ): UpdateResponse

    @POST("cart/delete.php")
    suspend fun deleteCart(
        @Body deleteRequest: DeleteRequest
    ): Response<ApiResponse>

    @POST("cart/create.php")
    suspend fun addToCart(
        @Body cart: Cart
    ): AddToCartResponse
}