package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.Notice
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class updateNoticeResponse(
    val success: Boolean,
    val message: String
)

data class NoticeResponse(
    val notice: List<Notice>
)

interface NoticeAPIService {
    @GET("notice/getNoticeByIdCustomer.php")
    suspend fun getNoticeByIdCustomer(
        @Query("idUser") idUser: String?
    ): NoticeResponse

    @PUT("notice/update.php")
    suspend fun updateNotice(
        @Body notice: Notice
    ): updateNoticeResponse

    @POST("notice/create.php")
    suspend fun addNotice(
        @Body notice : Notice
    ): updateNoticeResponse
}