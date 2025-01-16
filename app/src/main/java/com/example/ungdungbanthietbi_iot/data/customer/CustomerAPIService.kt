package com.example.ungdungbanthietbi_iot.data.customer

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

data class CustomerResponse(
    val customer: List<Customer>
)

interface CustomerAPIService {
    // Lấy tất cả khách hàng
    @GET("customer/read.php")
    fun getAllCustomer(): Call<CustomerResponse>

    @GET("customer/show.php")
    suspend fun getCustomerById(
        @Query("id") id: String
    ): Customer

    @GET("customer/getCustomerByIdOrder.php")
    suspend fun getCustomerByIdOrder(
        @Query("id") id: Int
    ): Customer
}