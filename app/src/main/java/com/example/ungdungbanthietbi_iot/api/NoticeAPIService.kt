package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Notice
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


data class SendTokenRequest(
    val deviceToken: String,
)
data class NoticeResponse(
    val status_code: Int,
    val data: List<Notice>,
)

data class ReadNNotice(
    val status_code: Int,
    val data: MessageData
)

data class MessageData(
    val message: String,
)
interface NoticeAPIService {

    @POST("auth/update-device-token")
    suspend fun sendTokenDevice(
        @Header("Authorization") token: String,
        @Body request : SendTokenRequest
    ): Response<Unit>
}

interface NoticeAPIServiceEcom {
    @PATCH("notification/read-notification/{id}")
    suspend fun readNotification(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<Unit>

    @GET("notification")
    suspend fun getNotifications(
        @Header("Authorization") token: String,
        @Query("type") type: String,
    ): NoticeResponse

    @POST("notification/fcm-token")
    suspend fun sendToken(
        @Header("Authorization") token: String,
        @Body request : SendTokenRequest
    ): Response<Unit>
}