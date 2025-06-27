package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET("categories/")
    suspend fun getCategories(): Response<CategoryResponse>
}