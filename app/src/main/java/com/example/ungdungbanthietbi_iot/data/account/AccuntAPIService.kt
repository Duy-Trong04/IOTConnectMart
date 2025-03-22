package com.example.ungdungbanthietbi_iot.data.account

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


data class CheckLoginResponse(
    val result: Boolean,
    val message: String? = null
)

data class CheckAccount(
    val result: Boolean,
)

data class AddAccountResponse(
    val success: Boolean,
    val message: String
)
data class accountUpdateResponse(
    val success: Boolean,
    val message: String
)

interface AccuntAPIService {
    @GET("account/check_account.php")
    suspend fun check_Login(
        @Query("username") username: String,
        @Query("password") password: String
    ): CheckLoginResponse
    

    @POST("account/check_Dk.php")
    suspend fun checkAccount_Dk(
        @Body account: AddAccount
    ): Boolean

    @GET("account/show.php")
    suspend fun getAccountByUsername(
        @Query("username") username: String
    ): Account

    @POST("account/create.php")
    suspend fun addAccount(
        @Body account: AddAccount
    ): AddAccountResponse

    @PUT("account/updatePassword.php")
    suspend fun updatePassword(
        @Body account: UpdatePassword
    ): Boolean

    @PUT("account/update.php")
    suspend fun updateAccount(
        @Body account: Account
    ): accountUpdateResponse
}