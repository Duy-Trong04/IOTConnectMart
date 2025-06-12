package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Cart
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class AddCartRequest(
    val customer_id :String,
    val product_id: Int,
    val quantity: Int,
)
data class AddToCartResponse(
    val status_code: Int,
    val errors: List<ErrorToCart>
)
data class ErrorToCart(
    val code: Int,
    val message: String,
)
data class CartResponse(
    val status_code: Int,
    val data: List<ProductInCart>,
    val errors: List<ErrorToCart>
)
data class ProductInCart(
    val id: Int,
    val image: String?,
    val name: String,
    val selling_price: Int,
    val status: Int,
    val stock: String,
    var quantity: Int,
    val selected: Boolean
)

interface CartAPIService {
    @POST("cart/")
    suspend fun addToCart(
        @Body request: AddCartRequest
    ): AddToCartResponse

    @GET("cart/customer/{customer_id}")
    suspend fun getCartProducts(
        @Path("customer_id") customer_id: String
    ): CartResponse

    @PUT("cart/update-quantity")
    suspend fun updateQuantity(
        @Body request: AddCartRequest
    ): AddToCartResponse

    @DELETE("cart/customer/{customer_id}/product/{product_id}")
    suspend fun removeCart(
        @Path("customer_id") customer_id: String,
        @Path("product_id") product_id: Int
    ): AddToCartResponse
}