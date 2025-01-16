package com.example.ungdungbanthietbi_iot.data.customer

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class CustomerResponse(
    val customer: List<Customer>
)

data class CheckCustomer(
    val result: Boolean,
)

data class AddCustomerResponse(
    val success: Boolean,
    val message: String
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

    @GET("customer/getCustomerReviewDeviceByIdDevice.php")
    suspend fun getCustomerReviewDeviceByIdDevice(
        @Query("idDevice") idDevice: Int
    ): CustomerResponse
    @POST("customer/check_Dk.php")
    suspend fun check_Dk(
        @Body customer: AddCustomer
    ): Boolean

    @POST("customer/create.php")
    suspend fun addCustomer(
        @Body customer: AddCustomer
    ): AddCustomerResponse

    @PUT("customer/updateUsername.php")
    suspend fun updateUsername(
        @Body customer: Username
    ): Boolean

    @PUT("customer/updateEmail.php")
    suspend fun updateEmail(
        @Body customer: Email
    ): Boolean

    @PUT("customer/updatePhone.php")
    suspend fun updatePhone(
        @Body customer: Phone
    ): Boolean

    @PUT("customer/updateBirthdate.php")
    suspend fun updateBirthdate(
        @Body customer: Birthdate
    ): Boolean

    @PUT("customer/updateGender.php")
    suspend fun updateGender(
        @Body customer: Gender
    ): Boolean

}