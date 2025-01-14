package com.example.ungdungbanthietbi_iot.data.customer

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import com.example.ungdungbanthietbi_iot.data.account.AddAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerViewModel:ViewModel() {
    var customer by mutableStateOf<Customer?>(null)
        private set

    var khachhangUpdateResult by mutableStateOf("")
        private set

    var customerAddResult by mutableStateOf("")

    private val _customerCheckResult = mutableStateOf<Boolean?>(null)
    val customerCheckResult: State<Boolean?> = _customerCheckResult

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

    //Them vào customer
    fun addToCustomer(customer: AddCustomer) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm account trên server
                val response = RetrofitClient.customerAPIService.addCustomer(customer)
                customerAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddToCustomer", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun check_Dk(customer: AddCustomer) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.customerAPIService.check_Dk(customer)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                    _customerCheckResult.value = response
                Log.d("AccountViewModel", "check_Dkc: ${response}")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                _customerCheckResult.value = false
            }
        }
    }
}