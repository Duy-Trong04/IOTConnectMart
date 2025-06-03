package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.AddLikedRequest
import com.example.ungdungbanthietbi_iot.api.AddLikedResponse1
import com.example.ungdungbanthietbi_iot.api.DeleteLikedResponse1
import com.example.ungdungbanthietbi_iot.api.LikedProduct
import com.example.ungdungbanthietbi_iot.api.LikedResponse1
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Liked
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikedViewModel:ViewModel() {
    var listLiked by mutableStateOf<List<LikedProduct>>(emptyList())

    suspend fun getLikedProducts(customerId: String): Result<LikedResponse1> {
        return try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.likedAPIService.getLikedProducts(customerId)
            }
            Log.d("LikedViewModel", "API Success: ${response.data.data}")
            // Cập nhật listLiked
            listLiked = response.data.data
            Result.success(response)
        } catch (e: Exception) {
            Log.e("LikedViewModel", "Lỗi khi lấy danh sách yêu thích: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun addLikedProduct(customerId: String, productId: String): Result<AddLikedResponse1> {
        return try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.likedAPIService.addLikedProduct(AddLikedRequest(customerId, productId))
            }
            // Làm mới danh sách yêu thích sau khi thêm
            getLikedProducts(customerId)
            Result.success(response)
        } catch (e: Exception) {
            Log.e("LikedViewModel", "Lỗi khi thêm sản phẩm yêu thích: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deleteLikedProduct(customerId: String, productId: String): Result<DeleteLikedResponse1> {
        return try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.likedAPIService.deleteLikedProduct(customerId, productId)
            }
            // Làm mới danh sách yêu thích sau khi xóa
            getLikedProducts(customerId)
            Result.success(response)
        } catch (e: Exception) {
            Log.e("LikedViewModel", "Lỗi khi xóa sản phẩm yêu thích: ${e.message}")
            Result.failure(e)
        }
    }
}