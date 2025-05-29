package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Address
import com.example.ungdungbanthietbi_iot.models.AddressBook
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class AddressResponse(
    val address: List<Address>
)

data class addAddressResponse(
    val success: Boolean,
    val message: String
)


data class deleteAddressRequest(
    val id: Int
)

data class ApiResponse(
    val status_code: Int,
    val data: ResponseData
)

data class ResponseData(
    val data: CustomerData,
    val total_page: Int
)

data class CustomerData(
    val customer: Customer,
    val address_books: List<AddressBook>
)

data class Customer(
    val id: String,
    val name: String,
    val phone: String,
    val email: String
)

data class AddressDetailRES(
    val status_code: Int,
    val data: AddressBook
)


interface AddressAPIService {

    @GET("address-book/customer/{id}")
    suspend fun getCustomerAddressBook(@Path("id") id: String): ApiResponse

    @GET("address-book/detail/{id}")
    suspend fun getAddressById(
        @Path("id") id: Int
    ): AddressDetailRES

    @POST("address_book/create.php")
    suspend fun addAddress(
        @Body address: Address
    ): addAddressResponse

    @PUT("address_book/update.php")
    suspend fun updateAddress(
        @Body address: Address
    ): addAddressResponse

    @GET("customer/{id}")
    suspend fun getAddressByIdCustomer(
        @Query("idCustomer") idCustomer: String?
    ): AddressResponse

    @GET("address_book/getAddressDefault.php")
    suspend fun getAddressDefault(
        @Query("idCustomer") idCustomer: String,
        @Query("isDefault") isDefault: Int
    ): Address

    @PUT("address_book/updateAddressDefault.php")
    suspend fun updateAddressDefault(
        @Body idCustomer: String
    ): addAddressResponse

    @POST("address_book/delete.php")
    suspend fun deleteAddress(
        @Body id: deleteAddressRequest
    ): Response<ApiResponse1>

    @GET("address_book/getAddressByIdOrder.php")
    suspend fun getAddressByIdOrder(
        @Query("id") id: Int
    ): Address
}