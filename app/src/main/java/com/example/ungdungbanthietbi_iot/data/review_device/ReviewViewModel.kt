package com.example.ungdungbanthietbi_iot.data.review_device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import com.example.ungdungbanthietbi_iot.data.liked.Liked
import com.example.ungdungbanthietbi_iot.data.order.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel:ViewModel() {
    var listReview: List<Review> by mutableStateOf(emptyList())

    var listReviewDaDanhGia by mutableStateOf<List<Review>>(emptyList())
        private set

    var reviewAddResult by mutableStateOf("")
    var reviewUpdateResult by mutableStateOf("")

    var review by mutableStateOf<Review?>(null)
        private set

    private val _reviewExistsMap = mutableStateMapOf<Int, Boolean?>()
    val reviewExistsMap: SnapshotStateMap<Int, Boolean?> = _reviewExistsMap

    private val _reviewExistsMap2 = mutableStateMapOf<Int, Boolean?>()
    val reviewExistsMap2: SnapshotStateMap<Int, Boolean?> = _reviewExistsMap2

    fun getReviewById(id: Int) {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.reviewAPIService.getReviewByIdReview(id)
                review = result
            } catch (e: Exception) {
                Log.e("API", "Lỗi: ${e.message}")
            }
        }
    }
    // Khởi tạo key với giá trị null
    fun initReviewCheck(idDevice: Int) {
        _reviewExistsMap[idDevice] = null
    }
    fun checkReview(idCustomer: String, idDevice: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.reviewAPIService.checkReview2(idCustomer, idDevice, 1)
                if (response.success) {
                    _reviewExistsMap[idDevice] = response.review_exists
                } else {
                    _reviewExistsMap[idDevice] = false
                    Log.e("CheckReview", "Phản hồi không thành công hoặc body rỗng.")
                }
            } catch (e: Exception) {
                _reviewExistsMap[idDevice] = false
                Log.e("CheckReview", "Lỗi khi gọi API: ${e.message}", e)
            }
        }
    }
    fun checkReview2(idCustomer: String, idDevice: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.reviewAPIService.checkReview2(idCustomer, idDevice, 2)
                if (response.success) {
                    _reviewExistsMap2[idDevice] = response.review_exists
                } else {
                    _reviewExistsMap2[idDevice] = false
                    Log.e("CheckReview", "Phản hồi không thành công hoặc body rỗng.")
                }
            } catch (e: Exception) {
                _reviewExistsMap2[idDevice] = false
                Log.e("CheckReview", "Lỗi khi gọi API: ${e.message}", e)
            }
        }
    }

    // Hàm suspend trả về kết quả trực tiếp
    suspend fun checkReviewDirect(idCustomer: String, idDevice: Int, status: Int): Review? {
        return try {
            val response = RetrofitClient.reviewAPIService.checkReview(idCustomer, idDevice, status)
            if (response.success) {
                // Nếu thành công, trả về đối tượng review (có thể null nếu chưa có review)
                response.review
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ReviewViewModel", "Lỗi checkReviewDirect: ${e.message}")
            null
        }
    }



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

    fun getReviewByIdCustomerDaDanhGia(idCustomer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = RetrofitClient.reviewAPIService.getReviewByIdCustomer(idCustomer, 1)
                // CLEAR trước khi gán
                withContext(Dispatchers.Main) {
                    listReviewDaDanhGia = resp
                }
            } catch (e: Exception) {
                Log.e("ReviewVM", "Error getting reviews (1):", e)
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
                if (response.success) {
                    reviewUpdateResult = "Cập nhật thành công: ${response.message}"

                } else {
                    reviewUpdateResult = "Cập nhật thất bại: ${response.message}"

                }
            } catch (e: Exception) {
                reviewUpdateResult = "Lỗi khi cập nhật review: ${e.message}"
                Log.e("Order Error", "Lỗi khi cập nhật review: ${e.message}")

            }
        }
    }
}