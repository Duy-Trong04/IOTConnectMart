package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.AddLikedRequest
import com.example.ungdungbanthietbi_iot.api.LikedProduct
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LikedViewModel:ViewModel() {
    private val _listLiked = MutableStateFlow<List<LikedProduct>>(emptyList())
    val listLiked: StateFlow<List<LikedProduct>> get() = _listLiked.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getLikedByIdCustomer(customer_id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.likedAPIService.getLikedProducts(customer_id)
                if(response.status_code == 200){
                    _listLiked.value = response.data.data
                }
                else{
                    _listLiked.value = emptyList()
                }
            } catch (e: Exception) {
                _listLiked.value = emptyList()
            }
        }
    }

    fun deleteLiked(customer_id: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.likedAPIService.deleteLikedProduct(customer_id, id)
                if(response.status_code == 201){
                    _error.value = null
                }
                else{
                    _error.value = "Failed to deleted liked, status: ${response.status_code}"
                    Log.e("LikedViewModel", "Failed to deleted liked, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _error.value = "Error deleted liked: ${e.message}"
                Log.e("Deleted Liked", "Lỗi kết nối: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun addLiked(request: AddLikedRequest) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = RetrofitClient.likedAPIService.addLikedProduct(request)
                if(response.status_code == 201){
                    _error.value = null
                }
                else{
                    _error.value = "Failed to create liked, status: ${response.status_code}"
                    Log.e("LikedViewModel", "Failed to create liked, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _error.value = "Error creating liked: ${e.message}"
                Log.e("Add Liked", "Lỗi kết nối: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }

//    //Xóa khỏi giỏ hàng
//    fun deleteLikedByCustomer(idCustomer: String, idDevice: Int) {
//        viewModelScope.launch {
//            try {
//                val deleteRequest = DeleteidDeviceResponse(idCustomer, idDevice)
//                val response = RetrofitClient.likedAPIService.deleteLikedByCustomer(deleteRequest)
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    if (apiResponse?.message == "Liked Deleted") {
//                        // Cập nhật lại giỏ hàng trong ViewModel
//                        listLiked = listLiked.filter { it.idCustomer != idCustomer && it.idDevice != idDevice }
//                        Log.d("LikedViewModel", "Liked đã được xóa")
//                    } else {
//                        Log.e("LikedViewModel", "Lỗi: ${apiResponse?.message}")
//                    }
//                } else {
//                    Log.e("LikedViewModel", "Error: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("LikedViewModel", "Exception: ${e.message}")
//            }
//        }
//    }
}