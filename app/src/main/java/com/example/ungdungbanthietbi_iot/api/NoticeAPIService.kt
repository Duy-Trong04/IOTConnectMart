package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Notice
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class updateNoticeResponse(
    val success: Boolean,
    val message: String
)

data class SendTokenRequest(
    val deviceToken: String,
)

data class DeviceTokenResponse(
    val success: String,
    val message: String
)

interface NoticeAPIService {

    @PUT("notice/update.php")
    suspend fun updateNotice(
        @Body notice: Notice
    ): updateNoticeResponse

    @POST("auth/update-device-token")
    suspend fun sendTokenDevice(
        @Header("Authorization") token: String,
        @Body request : SendTokenRequest
    ): Response<Unit>
}

interface NoticeAPIServiceEcom {
    @POST("notification/fcm-token")
    suspend fun sendToken(
        @Header("Authorization") token: String,
        @Body request : SendTokenRequest
    ): Response<Unit>
}