package com.example.ungdungbanthietbi_iot.data.cart

import retrofit2.http.GET
import retrofit2.http.POST

data class CartResponse(
    val protocol:String,
    val code:String,
    var url:String
)

interface CartAPIService {
    @GET("cart/")
    suspend fun getAllCart():List<Cart>
}