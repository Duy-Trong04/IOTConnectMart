package com.example.ungdungbanthietbi_iot.data.device

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Constant{
    const val BASE_URL = "http://10.0.2.2:3000/api/"
}

object DeviceRetrofitClient {
    val deviceAPIService:DeviceAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(DeviceAPIService::class.java)
    }
}