package com.example.ungdungbanthietbi_iot.data.order

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel:ViewModel() {
    var orderAddResult by mutableStateOf("")


    private val _listOrderOfCustomer = MutableStateFlow<List<Order>>(emptyList())
    val listOrderOfCustomer: StateFlow<List<Order>> = _listOrderOfCustomer


    var id by mutableStateOf(0)

    var order by mutableStateOf<Order?>(null)
        private set

    fun getOrderById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                order = RetrofitClient.orderAPIService.getOrderById(id)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error getting Order", e)
            }
        }
    }

    fun getOrderByCustomer(idCustomer: String, status: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.orderAPIService.getOrderByCustomer(idCustomer, status)
                }
                _listOrderOfCustomer.value = response.order ?: emptyList() // Cập nhật StateFlow
            } catch (e: Exception) {
                Log.e("Order Error", "Lỗi khi lấy order: ${e.message}")
                _listOrderOfCustomer.value = emptyList() // Gán danh sách rỗng khi có lỗi
            }
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = RetrofitClient.orderAPIService.addOrder(order)
                orderAddResult = if (response.success) {
                    "Thành công: ${response.message}"
                } else {
                    "Thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddOrder", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    // Xóa hóa đơn
    fun deleteOrder(id: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = orderDeleteRequest(id)
                val response = RetrofitClient.orderAPIService.deleteOrder(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Order Deleted") {
                        Log.d("OrderViewModel", "Order đã được xóa")
                        // Cập nhật danh sách sau khi xóa
                        _listOrderOfCustomer.value = _listOrderOfCustomer.value.filter { it.id != id }
                    } else {
                        Log.e("OrderViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("OrderViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Exception: ${e.message}")
            }
        }
    }

    // Cập nhật hóa đơn
    fun updateOrder(order: Order) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.orderAPIService.updateOrder(order)
                }
                orderAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                orderAddResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("Order Error", "Lỗi khi cập nhật order: ${e.message}")
            }
        }
    }
}