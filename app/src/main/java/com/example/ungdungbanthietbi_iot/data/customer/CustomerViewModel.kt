package com.example.ungdungbanthietbi_iot.data.customer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import com.example.ungdungbanthietbi_iot.data.device.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerViewModel:ViewModel() {
    var customer by mutableStateOf<Customer?>(null)
        private set
    var listCustomerByOrder by mutableStateOf<List<Customer>>(emptyList())
    var khachhangUpdateResult by mutableStateOf("")
        private set

    val allKhachHang = liveData(Dispatchers.IO) {
        try {
            // Gọi API lấy danh sách khách hàng
            val response = RetrofitClient.customerAPIService.getAllCustomer().execute()
            if (response.isSuccessful) {
                emit(response.body()?.customer ?: emptyList())  // Trả dữ liệu vào LiveData
            } else {
                emit(emptyList())  // Trả danh sách trống nếu phản hồi không thành công
            }
        } catch (e: Exception) {
            emit(emptyList())  // Trả danh sách trống nếu có lỗi
        }
    }
    fun getCustomerById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                customer = RetrofitClient.customerAPIService.getCustomerById(id)
            } catch (e: Exception) {
                Log.e("CustomerViewModel", "Error getting customer", e)
            }
        }
    }

    fun getCustomerByIdOrder(id: Int) {
        viewModelScope.launch {
            try {
                customer = RetrofitClient.customerAPIService.getCustomerByIdOrder(id)
            } catch (e: Exception) {
                Log.e("CustomerViewModel", "Error getting customer", e)
            }
        }
    }
}