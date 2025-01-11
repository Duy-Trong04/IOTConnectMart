package com.example.ungdungbanthietbi_iot.data.account


import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("account/check_account.php")
    suspend fun KiemTraLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): Account

    @GET("account/show.php")
    suspend fun getTaiKhoanByTentaikhoan(
        @Query("username") username: String
    ): Account
}
