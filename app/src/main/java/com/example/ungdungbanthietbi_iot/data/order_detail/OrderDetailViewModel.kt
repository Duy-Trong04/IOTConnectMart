package com.example.ungdungbanthietbi_iot.data.order_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.launch

class OrderDetailViewModel:ViewModel() {
    var orderDetailAddResult by mutableStateOf("")

    fun addOrderDetail(orderDetail: OrderDetail) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = RetrofitClient.orderDetailAPIService.addOrderDetail(orderDetail)
                orderDetailAddResult = if (response.success) {
                    "Thành công: ${response.message}"
                } else {
                    "Thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddOrderDetail", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}