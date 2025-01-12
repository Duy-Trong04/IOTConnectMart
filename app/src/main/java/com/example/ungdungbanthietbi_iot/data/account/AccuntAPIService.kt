package com.example.ungdungbanthietbi_iot.data.account

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query


data class CheckLoginResponse(
    val result: Boolean,
    val message: String? = null
)

interface AccuntAPIService {
    @GET("account/check_account.php")
    suspend fun check_Login(
        @Query("username") username: String,
        @Query("password") password: String
    ): CheckLoginResponse

    @GET("account/show.php")
    suspend fun getAccountByUsername(
        @Query("username") username: String
    ): Account

//    @PUT("TaiKhoan/update.php")
//    suspend fun updateTaiKhoan(
//        @Body taikhoan: Account
//    ): taikhoanUpdateResponse
}