package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.SlideShow
import retrofit2.http.GET


interface SlideShowAPIService {
    @GET("slideshow/read.php")
    suspend fun getAllSlideShow():List<SlideShow>
}