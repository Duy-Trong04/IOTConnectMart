package com.example.ungdungbanthietbi_iot.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class PaymentRequest(
    val amount: String,
    val bankCode: String,
    val returnUrl: String = "myapp://payment"
)

data class PaymentResponse(
    val paymentUrl: String
)

interface VNPayAPIService {
    @POST("order/create_payment_url")
    suspend fun createPayment(@Body request: PaymentRequest): PaymentResponse
}