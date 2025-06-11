package com.example.ungdungbanthietbi_iot.config

import com.example.ungdungbanthietbi_iot.api.AccuntAPIService
import com.example.ungdungbanthietbi_iot.api.AddressAPIService
import com.example.ungdungbanthietbi_iot.api.CartAPIService
import com.example.ungdungbanthietbi_iot.api.CategoryApi
import com.example.ungdungbanthietbi_iot.api.CustomerAPIService
import com.example.ungdungbanthietbi_iot.api.DeviceAPIService
import com.example.ungdungbanthietbi_iot.api.ImageAPIService
import com.example.ungdungbanthietbi_iot.api.LikedAPIService
import com.example.ungdungbanthietbi_iot.api.NoticeAPIService
import com.example.ungdungbanthietbi_iot.api.OrderAPIService
import com.example.ungdungbanthietbi_iot.api.OrderDetailAPIService
import com.example.ungdungbanthietbi_iot.api.ReviewAPIService
import com.example.ungdungbanthietbi_iot.api.SlideShowAPIService
import com.example.ungdungbanthietbi_iot.api.VNPayAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Constant{
    const val BASE_URL = "http://10.0.2.2:8081/api/" //10.0.2.2
}

object RetrofitClient {
    val deviceAPIService: DeviceAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(DeviceAPIService::class.java)
    }

    val slideshowAPIService: SlideShowAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(SlideShowAPIService::class.java)
    }
    val imageAPIService: ImageAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ImageAPIService::class.java)
    }
    val reviewAPIService: ReviewAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ReviewAPIService::class.java)
    }
    val accountAPIService: AccuntAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccuntAPIService::class.java)
    }
    val customerAPIService: CustomerAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CustomerAPIService::class.java)
    }
    val cartAPIService: CartAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CartAPIService::class.java)
    }
    val orderAPIService: OrderAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(OrderAPIService::class.java)
    }
    val orderDetailAPIService: OrderDetailAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(OrderDetailAPIService::class.java)
    }

    val addressAPIService: AddressAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AddressAPIService::class.java)
    }
    val likedAPIService: LikedAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(LikedAPIService::class.java)
    }

    val noticeAPIService: NoticeAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(NoticeAPIService::class.java)
    }
    val categoryAPIService: CategoryApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CategoryApi::class.java)
    }

    val verifyOtp: AccuntAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccuntAPIService::class.java)
    }

    val authApiService: AccuntAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccuntAPIService::class.java)
    }

    val resetPassword: AccuntAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccuntAPIService::class.java)
    }
    val vnPayService: VNPayAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(VNPayAPIService::class.java)
    }
}