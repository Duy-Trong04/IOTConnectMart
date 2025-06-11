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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import com.example.ungdungbanthietbi_iot.api.CheckoutResponse
import com.example.ungdungbanthietbi_iot.api.OrderData
import com.example.ungdungbanthietbi_iot.api.OrderDataCheckOut
import com.example.ungdungbanthietbi_iot.api.OrderRequestCancel
import com.example.ungdungbanthietbi_iot.api.OrderResponse
import com.example.ungdungbanthietbi_iot.models.CheckoutRequest
import com.example.ungdungbanthietbi_iot.models.Notice
import com.example.ungdungbanthietbi_iot.models.Order
import com.example.ungdungbanthietbi_iot.utils.getCurrentTimestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow

sealed class CheckoutState {
    object Idle : CheckoutState()
    object Loading : CheckoutState()
    data class Success(val orderData: OrderDataCheckOut) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}

class OrderViewModel:ViewModel() {

    private val _listAllOrderOfCustomer = MutableStateFlow<List<Order>>(emptyList())
    val listAllOrderOfCustomer: StateFlow<List<Order>> = _listAllOrderOfCustomer

    private val _listOrders = MutableStateFlow<OrderResponse?>(null)
    val listOrders: StateFlow<OrderResponse?> = _listOrders.asStateFlow()

    fun getOrdersByCustomer(idCustomer: String) {
        viewModelScope.launch {
            Log.i("OrderViewModel", "Starting to fetch orders for customer: $idCustomer")
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.orderAPIService.getOrdersByCustomer(idCustomer)
                }
                Log.d("OrderViewModel", "Lấy được ${response.data.data.size} đơn hàng: ${response.data.data.map { it.id to it.status }}")
                _listOrders.value = response
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error fetching orders: ${e.stackTraceToString()}")
                _listOrders.value = OrderResponse(
                    status_code = 500,
                    data = OrderData(data = emptyList(), total_page = 0)
                )
            }
        }
    }

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState

    fun createOrder(checkoutRequest: CheckoutRequest) {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading
            try {
                val response: CheckoutResponse = RetrofitClient.orderAPIService.createOrder(checkoutRequest)
                Log.d("OrderViewModel", "API Response: $response")
                when (response.status_code) {
                    200 -> {
                        if (response.error_code == 0) {
                            _checkoutState.value = CheckoutState.Success(response.data)
                        } else {
                            _checkoutState.value = CheckoutState.Error("Lỗi API: ${response.error_code}")
                        }
                    }
                    400 -> {
                        val errorMessages = response.data_errors?.joinToString { error ->
                            "Sản phẩm ${error.product_name}: ${error.errors.joinToString { it.message }}"
                        } ?: "Lỗi không xác định"
                        _checkoutState.value = CheckoutState.Error("Yêu cầu không hợp lệ: $errorMessages")
                    }
                    else -> {
                        _checkoutState.value = CheckoutState.Error("Gọi API thất bại: ${response.status_code}")
                    }
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error creating order: ${e.message}")
                _checkoutState.value = CheckoutState.Error("Error: ${e.message}")
            }
        }
    }
    // MutableState để lưu trạng thái UI
    private val _statusCode = mutableStateOf<Int?>(null)
    val statusCode: State<Int?> = _statusCode

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Thêm state để trigger refresh
    private val _shouldRefresh = mutableStateOf(false)
    val shouldRefresh: State<Boolean> = _shouldRefresh
    // Cập nhật hóa đơn
    fun cancelOrder(id: String) {
        viewModelScope.launch {
            try {
                val request = OrderRequestCancel(id)
                Log.d("OrderViewModel", "Sending request: $request")
                val response = RetrofitClient.orderAPIService.cancelOrder(request)
                Log.d("OrderViewModel", "Response code: ${response.code()}")
                _statusCode.value = response.code()
                _errorMessage.value = null
                if (response.code() == 200) {
                    // Trigger refresh hoặc xóa item
                    _shouldRefresh.value = true
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _statusCode.value = null
            }
        }
    }
    // Hàm reset refresh state
    fun resetRefresh() {
        _shouldRefresh.value = false
    }
}