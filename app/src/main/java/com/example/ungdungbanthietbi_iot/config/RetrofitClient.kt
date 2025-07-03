package com.example.ungdungbanthietbi_iot.config

import com.example.ungdungbanthietbi_iot.api.AccountAPIService
import com.example.ungdungbanthietbi_iot.api.AccountAPIServiceIOT
import com.example.ungdungbanthietbi_iot.api.AddressAPIService
import com.example.ungdungbanthietbi_iot.api.AddressPublic
import com.example.ungdungbanthietbi_iot.api.CartAPIService
import com.example.ungdungbanthietbi_iot.api.CategoryApi
import com.example.ungdungbanthietbi_iot.api.CustomerAPIService
import com.example.ungdungbanthietbi_iot.api.DeviceAPIService
import com.example.ungdungbanthietbi_iot.api.ImageAPIService
import com.example.ungdungbanthietbi_iot.api.LikedAPIService
import com.example.ungdungbanthietbi_iot.api.NoticeAPIService
import com.example.ungdungbanthietbi_iot.api.NoticeAPIServiceEcom
import com.example.ungdungbanthietbi_iot.api.OrderAPIService
import com.example.ungdungbanthietbi_iot.api.ReviewAPIService
import com.example.ungdungbanthietbi_iot.api.SlideShowAPIService
import com.example.ungdungbanthietbi_iot.api.VNPayAPIService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Constant{
    const val BASE_URL = "https://sns-e-com-backend.up.railway.app/api/" //10.0.2.2
    //const val BASE_URL = "http://10.0.2.2:8081/api/" //10.0.2.2
    const val BASE_URL_ADDRESS_PUBLIC = "https://online-gateway.ghn.vn/"
    const val BASE_URL_IOT = "https://iothomeconnectapiv2-production.up.railway.app/api/"
}

object RetrofitClient {
    val deviceAPIService: DeviceAPIService by lazy {
        // Tạo OkHttpClient với các thiết lập timeout
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Timeout khi kết nối, ví dụ: 30 giây
            .readTimeout(60, TimeUnit.SECONDS)   // Timeout khi đọc dữ liệu
            .writeTimeout(60, TimeUnit.SECONDS)  // Timeout khi ghi dữ liệu
            .build()

        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient) // Thêm OkHttpClient vào Retrofit
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
    val accountAPIService: AccountAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccountAPIService::class.java)
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

    val addressAPIService: AddressAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AddressAPIService::class.java)
    }
    val likedAPIService: LikedAPIService by lazy {
        // Tạo OkHttpClient với các thiết lập timeout
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Timeout khi kết nối, ví dụ: 30 giây
            .readTimeout(60, TimeUnit.SECONDS)   // Timeout khi đọc dữ liệu
            .writeTimeout(60, TimeUnit.SECONDS)  // Timeout khi ghi dữ liệu
            .build()

        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient) // Thêm OkHttpClient vào Retrofit
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(LikedAPIService::class.java)
    }

    val noticeAPIService: NoticeAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_IOT)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(NoticeAPIService::class.java)
    }
    val noticeAPIServiceEcom: NoticeAPIServiceEcom by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(NoticeAPIServiceEcom::class.java)
    }
    val accountAPIServiceIOT: AccountAPIServiceIOT by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_IOT)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccountAPIServiceIOT::class.java)
    }
    val categoryAPIService: CategoryApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CategoryApi::class.java)
    }

    val verifyOtp: AccountAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccountAPIService::class.java)
    }

    val authApiService: AccountAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccountAPIService::class.java)
    }

    val resetPassword: AccountAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AccountAPIService::class.java)
    }
    val vnPayService: VNPayAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(VNPayAPIService::class.java)
    }

    val addressPublic: AddressPublic by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_ADDRESS_PUBLIC)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AddressPublic::class.java)
    }
}