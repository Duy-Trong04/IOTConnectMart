package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.SlideShow
import retrofit2.http.GET
import retrofit2.http.Path

data class SlideShowResponse(
    val status_code: Int,
    val data: SlideShowData
)
data class SlideShowData(
    val data: List<SlideShow>,
    val total_page: Int
)

data class SlideShowDetailResponse(
    val status_code: Int,
    val data: SlideShow
)

interface SlideShowAPIService {
    @GET("slideshow/")
    suspend fun getAllSlideShow() : SlideShowResponse

    @GET("slideshow/{id}")
    suspend fun getSlideShowById(
        @Path("id") id: Int
    ) : SlideShowDetailResponse
}