package com.example.ungdungbanthietbi_iot.data.review_device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import com.example.ungdungbanthietbi_iot.data.liked.Liked
import com.example.ungdungbanthietbi_iot.data.order.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel:ViewModel() {
    var listReview: List<Review> by mutableStateOf(emptyList())

    var listReviewChuaDanhGia: List<Review> by mutableStateOf(emptyList())
    var listReviewDaDanhGia: List<Review> by mutableStateOf(emptyList())

    var reviewAddResult by mutableStateOf("")
    var reviewUpdateResult by mutableStateOf("")

    fun getReviewByIdDevice(idDevice:String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                listReview = RetrofitClient.reviewAPIService.getReviewByIdDevice(idDevice)
            }
            catch (e:Exception){
                Log.e("DeviceViewModel", "Error getting image", e)
            }
        }
    }


    fun getReviewByIdCustomerChuaDanhGia(idCustomer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listReviewChuaDanhGia = RetrofitClient.reviewAPIService.getReviewByIdCustomer(idCustomer, 0)
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Error getting reviews not rated", e)
            }
        }
    }

    fun getReviewByIdCustomerDaDanhGia(idCustomer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listReviewDaDanhGia = RetrofitClient.reviewAPIService.getReviewByIdCustomer(idCustomer, 1)
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Error getting reviews rated", e)
            }
        }
    }

    fun addReview(review: Review) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.reviewAPIService.addReview(review)
                reviewAddResult = if (response.success) {
                    "Thêm thành công: ${response.message}"
                } else {
                    "Thêm thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("Add Review", "Lỗi kết nối: ${e.message}")
            }
        }
    }


    fun updateReview(review: Review) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.reviewAPIService.updateReview(review)
                }
                reviewUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                reviewUpdateResult = "Lỗi khi cập nhật review: ${e.message}"
                Log.e("Order Error", "Lỗi khi cập nhật review: ${e.message}")
            }
        }
    }
}