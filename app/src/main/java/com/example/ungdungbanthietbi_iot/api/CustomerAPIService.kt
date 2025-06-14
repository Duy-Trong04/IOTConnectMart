package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Customer
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

data class ResponseCustomer(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("data") val data: Customer
)

interface CustomerAPIService {
    // Lấy tất cả khách hàng
    @GET("customer/{id}")
    suspend fun getCustomerById9(
        @Path("id") id: String
    ): ResponseCustomer

    //Cập nhật thông tin khách hàng
    @PUT("customer/")
    suspend fun updateCustomer(
        @Body customer: Customer
    ): ResponseCustomer
}