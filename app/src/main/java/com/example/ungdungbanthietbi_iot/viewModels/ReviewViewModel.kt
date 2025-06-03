package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Review
import com.example.ungdungbanthietbi_iot.models.Reviews
import com.example.ungdungbanthietbi_iot.models.SlideShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant

class ReviewViewModel:ViewModel() {
    private val _listReviews = MutableStateFlow<List<Reviews>>(emptyList())
    // Public StateFlow for UI to observe
    val listReviews: StateFlow<List<Reviews>> = _listReviews.asStateFlow()

    private val _listAllReviews = MutableStateFlow<List<Reviews>>(emptyList())
    // Public StateFlow for UI to observe
    val listAllReviews: StateFlow<List<Reviews>> = _listAllReviews.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var listReviewDaDanhGia by mutableStateOf<List<Review>>(emptyList())
        private set

    private var reviewAddResult by mutableStateOf("")
    private var reviewUpdateResult by mutableStateOf("")

    var review by mutableStateOf<Review?>(null)
        private set

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

    fun getAllReviews(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.reviewAPIService.getAllReviews()
                Log.d("ReviewViewModel", "API response: $response")
                if (response.status_code == 200) {
                    _listAllReviews.value = response.data.data.sortedByDescending { review ->
                        try {
                            Instant.parse(review.created_at)
                        } catch (e: Exception) {
                            Instant.EPOCH
                        }
                    }
                    Log.d("ReviewViewModel", "Fetched and sorted reviews: ${_listAllReviews.value.size}")
                } else {
                    _listAllReviews.value = emptyList()
                    Log.e("ReviewViewModel", "Failed to fetch reviews, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _listAllReviews.value = emptyList()
                Log.e("ReviewViewModel", "Error fetching reviews", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getReviewByIdDevice(id:String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = RetrofitClient.reviewAPIService.getReviewByIdDevice(id)
                _listReviews.value = response.data.data
            }
            catch (e:Exception){
                _listReviews.value = emptyList()
                Log.e("ReviewViewModel", "Error getting reviews", e)
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