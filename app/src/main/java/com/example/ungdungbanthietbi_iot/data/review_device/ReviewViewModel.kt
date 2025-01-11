package com.example.ungdungbanthietbi_iot.data.review_device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel:ViewModel() {
    var listReview: List<Review> by mutableStateOf(emptyList())

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
}