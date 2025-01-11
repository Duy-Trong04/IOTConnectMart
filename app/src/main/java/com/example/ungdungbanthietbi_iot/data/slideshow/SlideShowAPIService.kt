package com.example.ungdungbanthietbi_iot.data.slideshow

import retrofit2.http.GET


interface SlideShowAPIService {
    @GET("slideshow/")
    suspend fun getAllSlideShow():List<SlideShow>
}