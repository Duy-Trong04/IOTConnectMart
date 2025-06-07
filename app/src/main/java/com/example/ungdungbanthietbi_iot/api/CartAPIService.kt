package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Cart
import retrofit2.http.Body
import retrofit2.http.POST

data class AddToCartResponse(
    val success: Boolean,
    val message: String
)
interface CartAPIService {
    @POST("cart/create.php")
    suspend fun addToCart(
        @Body cart: Cart
    ): AddToCartResponse
}