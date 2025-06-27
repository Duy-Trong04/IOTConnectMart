package com.example.ungdungbanthietbi_iot.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.ReviewRequestCreate
import com.example.ungdungbanthietbi_iot.api.ReviewRequestUpdate
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.ReviewDetail
import com.example.ungdungbanthietbi_iot.models.Reviews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class ReviewViewModel:ViewModel() {
    private val _listReviews = MutableStateFlow<List<Reviews>>(emptyList())
    val listReviews: StateFlow<List<Reviews>> = _listReviews.asStateFlow()

    private val _listAllReviews = MutableStateFlow<List<Reviews>>(emptyList())
    val listAllReviews: StateFlow<List<Reviews>> = _listAllReviews.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    var review by mutableStateOf<ReviewDetail?>(null)
        private set

    fun getReviewById(id: Int) {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.reviewAPIService.getReviewByIdReview(id)
                review = result.data
            } catch (e: Exception) {
                review = null
                Log.e("API", "Lỗi: ${e.message}")
            }
        }
    }

    @SuppressLint("NewApi")
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
            _isLoading.value = true
            try{
                val response = RetrofitClient.reviewAPIService.getReviewByIdDevice(id)
                _listReviews.value = response.data.data
                Log.e("ReviewViewModel", "Count ${response.data.data.size}")
            }
            catch (e:Exception){
                _listReviews.value = emptyList()
                Log.e("ReviewViewModel", "Error getting reviews", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addReview(request: ReviewRequestCreate) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.reviewAPIService.addReview(request)
                if(response.status_code == 201){
                    _error.value = null
                }
                else{
                    _error.value = "Failed to update review, status: ${response.status_code}"
                    Log.e("ReviewViewModel", "Failed to update review, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _error.value = "Error updating review: ${e.message}"
                Log.e("Add Review", "Lỗi kết nối: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }


    fun updateReview(request : ReviewRequestUpdate) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.reviewAPIService.updateReview(request)
                if (response.status_code == 200) {
                    _error.value = null
                } else {
                    _error.value = "Failed to update review, status: ${response.status_code}"
                    Log.e("ReviewViewModel", "Failed to update review, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _error.value = "Error updating review: ${e.message}"
                Log.e("ReviewViewModel", "Error updating review", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}