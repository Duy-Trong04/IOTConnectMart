package com.example.ungdungbanthietbi_iot.data.address_book

import com.example.ungdungbanthietbi_iot.data.cart.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
interface AddressAPIService {
    @GET("address_book/show.php")
    suspend fun getAddressById(
        @Query("id") id: Int
    ): Address

    @POST("address_book/create.php")
    suspend fun addAddress(
        @Body address: Address
    ): addAddressResponse

    @PUT("address_book/update.php")
    suspend fun updateAddress(
        @Body address: Address
    ): addAddressResponse

    @GET("address_book/getAddressByIdCustomer.php")
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
    ): Response<ApiResponse>

    @GET("address_book/getAddressByIdOrder.php")
    suspend fun getAddressByIdOrder(
        @Query("id") id: Int
    ): Address
}