package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableIntStateOf
import com.example.ungdungbanthietbi_iot.models.Notice
import com.example.ungdungbanthietbi_iot.models.Order
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestamp
import kotlinx.coroutines.delay

class OrderViewModel:ViewModel() {
    private var orderAddResult by mutableStateOf("")

    private lateinit var sharedPreferences: SharedPreferences
    // Khởi tạo SharedPreferences
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("order_status", Context.MODE_PRIVATE)
    }

    // Hàm kiểm tra trạng thái đơn hàng định kỳ
    fun startOrderStatusCheck(idCustomer: String) {
        viewModelScope.launch {
            while (true) {
                checkOrderStatusChanges(idCustomer)
                delay(300000) // Kiểm tra mỗi 5 phút
            }
        }
    }

    private val _listOrderOfCustomer = MutableStateFlow<List<Order>>(emptyList())
    val listOrderOfCustomer: StateFlow<List<Order>> = _listOrderOfCustomer

    private val _listAllOrderOfCustomer = MutableStateFlow<List<Order>>(emptyList())
    val listAllOrderOfCustomer: StateFlow<List<Order>> = _listAllOrderOfCustomer


    var id by mutableIntStateOf(0)

    var order by mutableStateOf<Order?>(null)
        private set

    private suspend fun checkOrderStatusChanges(idCustomer: String) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.orderAPIService.getAllOrderByCustomer(idCustomer)
            }
            val currentOrders = response.order ?: emptyList()
            _listAllOrderOfCustomer.value = currentOrders

            currentOrders.forEach { order ->
                val previousStatus = sharedPreferences.getInt("order_id_${order.id}", 0)
                if (previousStatus != order.status) {
                    val notice = createNoticeForOrderStatusChange(order)
                    // Gọi NoticeViewModel để thêm thông báo
                    val noticeViewModel = NoticeViewModel()
                    noticeViewModel.addNotice(notice)
                    // Lưu trạng thái mới vào SharedPreferences
                    sharedPreferences.edit().putInt("order_id_${order.id}", order.status).apply()
                }
            }
        } catch (e: Exception) {
            Log.e("OrderViewModel", "Error checking order status changes", e)
        }
    }

    private fun createNoticeForOrderStatusChange(order: Order): Notice {
        val noticeText = when (order.status) {
            1 -> "Đơn hàng #${order.id} của bạn đã được đặt thành công và đang chờ xác nhận."
            2 -> "Đơn hàng #${order.id} của bạn đã được xác nhận và đang chuẩn bị hàng."
            3 -> "Đơn hàng #${order.id} của bạn đã được giao cho đơn vị vận chuyển."
            4 -> "Vui lòng chỉ nhấn Đã nhận được hàng khi đơn hàng #${order.id} đã được giao đến bạn và sản phẩm không có vấn đề nào."
            5 -> "Đơn hàng #${order.id} của bạn đã được giao thành công đến bạn."
            6 -> "Đơn hàng #${order.id} của bạn đã bị hủy."
            else -> "Trạng thái đơn hàng #${order.id} đã thay đổi."
        }
        return Notice(
            id = 0, // Server sẽ sinh ID
            idUser = order.idCustomer,
            idRole = "customer",
            text = noticeText,
            type = "order_status_change",
            created_at = getCurrentTimestamp(),
            status = 1 // Chưa đọc
        )
    }

    fun getOrderById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                order = RetrofitClient.orderAPIService.getOrderById(id)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error getting Order", e)
            }
        }
    }

    fun getAllOrderByCustomer(idCustomer: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.orderAPIService.getAllOrderByCustomer(idCustomer)
                }
                _listAllOrderOfCustomer.value = response.order ?: emptyList() // Cập nhật StateFlow
            } catch (e: Exception) {
                Log.e("Order Error", "Lỗi khi lấy order: ${e.message}")
                _listAllOrderOfCustomer.value = emptyList() // Gán danh sách rỗng khi có lỗi
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