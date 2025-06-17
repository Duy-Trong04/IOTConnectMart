package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.AddressBook
import com.example.ungdungbanthietbi_iot.models.AddressResponsePublic
import com.example.ungdungbanthietbi_iot.models.District
import com.example.ungdungbanthietbi_iot.models.Province
import com.example.ungdungbanthietbi_iot.models.Ward
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class createAddressResponse(
    val status_code: Int,
    val data: AddressBook
)

data class CreateAddressRequest(
    val customer_id: String,
    val receiver_name: String,
    val phone: String,
    val district: String?,
    val city: String?,
    val ward: String?,
    val street: String?,
    val detail: String?,
    val is_default: Boolean
)
data class UpdateAddressRequest(
    val customer_id: String,
    val id: Int,
    val receiver_name: String,
    val phone: String,
    val district: String?,
    val city: String?,
    val ward: String?,
    val street: String?,
    val detail: String?,
    val is_default: Boolean
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

data class deleteResponse(
    val status_code: Int,
    val data: String
)
interface AddressAPIService {

    @GET("address-book/customer/{id}")
    suspend fun getCustomerAddressBook(@Path("id") id: String): ApiResponse

    @GET("address-book/detail/{id}")
    suspend fun getAddressById(
        @Path("id") id: Int
    ): AddressDetailRES

    @POST("address-book/")
    suspend fun createAddress(
        @Body request : CreateAddressRequest
    ): createAddressResponse

    @PUT("address-book/")
    suspend fun updateAddress(
        @Body request : UpdateAddressRequest
    ): createAddressResponse


    @DELETE("address-book/{customer_id}/{id}")
    suspend fun deleteAddress(
        @Path ("customer_id") customer_id: String,
        @Path ("id") id: Int,
    ): deleteResponse
}

interface AddressPublic {
    @GET("shiip/public-api/master-data/province")
    suspend fun getProvinces(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Token") token: String
    ): AddressResponsePublic<List<Province>>

    @GET("shiip/public-api/master-data/district")
    suspend fun getDistricts(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Token") token: String,
        @Query("province_id") provinceId: Int
    ): AddressResponsePublic<List<District>>

    @GET("shiip/public-api/master-data/ward")
    suspend fun getWards(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Token") token: String,
        @Query("district_id") districtId: Int
    ): AddressResponsePublic<List<Ward>>
}