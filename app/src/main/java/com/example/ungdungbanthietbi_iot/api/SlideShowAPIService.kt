package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.SlideShow
import retrofit2.http.GET

data class SlideShowResponse(
    val status_code: Int,
    val data: SlideShowData
)
data class SlideShowData(
    val data: List<SlideShow>,
    val total_page: Int
)

interface SlideShowAPIService {
    @GET("slideshow/")
    suspend fun getAllSlideShow() : SlideShowResponse
}