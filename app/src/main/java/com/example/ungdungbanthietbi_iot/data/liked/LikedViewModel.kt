package com.example.ungdungbanthietbi_iot.data.liked

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikedViewModel:ViewModel() {
    var listLiked by mutableStateOf<List<Liked>>(emptyList())

    var likedUpdateResult by mutableStateOf("")
    var likedAddResult by mutableStateOf("")

    fun getLikedByIdCustomer(idCustomer: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.likedAPIService.getLikedByIdCustomer(idCustomer)
                }
                listLiked = response.liked
            } catch (e: Exception) {
                Log.e("Liked Error", "Lỗi khi lấy Liked: ${e.message}")
            }
        }
    }

    // Cập nhật giỏ hàng đơn lẻ
    fun updateLiked(liked: Liked) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.likedAPIService.updateLiked(liked)
                }
                likedUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                likedUpdateResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("Liked Error", "Lỗi khi cập nhật Liked: ${e.message}")
            }
        }
    }

    // Cập nhật tất cả giỏ hàng
    fun updateAllLiked() {
        viewModelScope.launch {
            try {
                listLiked.forEach { liked ->
                    val response = RetrofitClient.likedAPIService.updateLiked(liked)
                    if (!response.success) {
                        // Nếu cập nhật thất bại, xử lý lỗi (hoặc thông báo lỗi)
                        Log.e("liked Error", "Cập nhật thất bại cho sản phẩm: ${liked.idDevice}")
                    }
                }
                likedUpdateResult = "Cập nhật likeed thành công"
            } catch (e: Exception) {
                likedUpdateResult = "Lỗi khi cập nhật toàn bộ like: ${e.message}"
                Log.e("Liked Error", "Lỗi khi cập nhật toàn bộ Liked: ${e.message}")
            }
        }
    }

    //Xóa khỏi giỏ hàng
    fun deleteLiked(id: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteRequest(id)
                val response = RetrofitClient.likedAPIService.deleteLiked(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Liked Deleted") {
                        // Cập nhật lại giỏ hàng trong ViewModel
                        listLiked = listLiked.filter { it.id != id }
                        Log.d("LikedViewModel", "Liked đã được xóa")
                    } else {
                        Log.e("LikedViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("LikedViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LikedViewModel", "Exception: ${e.message}")
            }
        }
    }

    //Them vào giỏ hàng
    fun addLiked(liked: Liked) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = RetrofitClient.likedAPIService.addliked(liked)
                likedAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddLiked", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    //Xóa khỏi giỏ hàng
    fun deleteLikedByCustomer(idCustomer: String, idDevice: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteidDeviceResponse(idCustomer, idDevice)
                val response = RetrofitClient.likedAPIService.deleteLikedByCustomer(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Liked Deleted") {
                        // Cập nhật lại giỏ hàng trong ViewModel
                        listLiked = listLiked.filter { it.idCustomer != idCustomer && it.idDevice != idDevice }
                        Log.d("LikedViewModel", "Liked đã được xóa")
                    } else {
                        Log.e("LikedViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("LikedViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LikedViewModel", "Exception: ${e.message}")
            }
        }
    }
}