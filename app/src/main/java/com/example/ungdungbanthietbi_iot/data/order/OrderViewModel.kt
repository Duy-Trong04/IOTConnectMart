package com.example.ungdungbanthietbi_iot.data.order

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

class OrderViewModel:ViewModel() {
    var orderAddResult by mutableStateOf("")

    var listOrderOfCustomer by mutableStateOf<List<Order>>(emptyList())
        private set

    fun getOrderByCustomer(idCustomer: String, status: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.orderAPIService.getOrderByCustomer(idCustomer, status)
                }
                listOrderOfCustomer = response.order ?: emptyList() // Nếu response.order là null, gán danh sách rỗng
            } catch (e: Exception) {
                Log.e("HoaDon Error", "Lỗi khi lấy hoadon: ${e.message}")
                listOrderOfCustomer = emptyList() // Gán danh sách rỗng khi có lỗi
            }
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = RetrofitClient.orderAPIService.addOrder(order)
                orderAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddToCart", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}