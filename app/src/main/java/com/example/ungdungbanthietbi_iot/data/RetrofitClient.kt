package com.example.ungdungbanthietbi_iot.data

import com.example.ungdungbanthietbi_iot.data.account.AccuntAPIService
import com.example.ungdungbanthietbi_iot.data.address_book.AddressAPIService
import com.example.ungdungbanthietbi_iot.data.cart.CartAPIService
import com.example.ungdungbanthietbi_iot.data.customer.CustomerAPIService
import com.example.ungdungbanthietbi_iot.data.device.DeviceAPIService
import com.example.ungdungbanthietbi_iot.data.image_device.ImageAPIService
import com.example.ungdungbanthietbi_iot.data.liked.LikedAPIService
import com.example.ungdungbanthietbi_iot.data.order.OrderAPIService
import com.example.ungdungbanthietbi_iot.data.order_detail.OrderDetailAPIService
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewAPIService
import com.example.ungdungbanthietbi_iot.data.slideshow.SlideShowAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Constant{
    const val BASE_URL = "http://10.0.2.2/IOT_ConnectMart_API/api/"
}

object RetrofitClient {
    val deviceAPIService: DeviceAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(DeviceAPIService::class.java)
    }

    val slideshowAPIService:SlideShowAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(SlideShowAPIService::class.java)
    }
    val imageAPIService:ImageAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ImageAPIService::class.java)
    }
    val reviewAPIService:ReviewAPIService by lazy {
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
}