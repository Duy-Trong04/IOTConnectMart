package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.AddCustomer
import com.example.ungdungbanthietbi_iot.models.Birthdate
import com.example.ungdungbanthietbi_iot.models.Customer
import com.example.ungdungbanthietbi_iot.models.Email
import com.example.ungdungbanthietbi_iot.models.Gender
import com.example.ungdungbanthietbi_iot.models.Phone
import com.example.ungdungbanthietbi_iot.models.Username
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerViewModel:ViewModel() {
    var customer by mutableStateOf<Customer?>(null)
        private set
    var listCustomerReviewDevice by mutableStateOf<List<Customer>>(emptyList())
    private var customerUpdateResult by mutableStateOf("")

    private var customerAddResult by mutableStateOf("")

    private val _customerCheckResult = mutableStateOf<Boolean?>(null)
    val customerCheckResult: State<Boolean?> = _customerCheckResult

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.customerAPIService.updateCustomer(customer)
                }
                customerUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                customerUpdateResult = "Lỗi khi cập nhật khách hàng: ${e.message}"
                Log.e("Update Error", "Lỗi khi cập nhật khách hàng: ${e.message}")
            }
        }
    }

    fun getCustomerReviewDeviceByIdDevice(idDevice: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.customerAPIService.getCustomerReviewDeviceByIdDevice(idDevice)
                }
                listCustomerReviewDevice = response.customer
            } catch (e: Exception) {
                Log.e("Customer Error", "Lỗi khi lấy customer", e)
            }
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
                Log.d("AccountViewModel", "check_Dkc: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                _customerCheckResult.value = false
            }
        }
    }

    fun updateUsername(customer: Username) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.customerAPIService.updateUsername(customer)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                //_customerCheckResult.value = response
                Log.d("AccountViewModel", "check_Dkc: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                //_customerCheckResult.value = false
            }
        }
    }

    fun updateEmail(email: Email) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.customerAPIService.updateEmail(email)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                //_customerCheckResult.value = response
                Log.d("AccountViewModel", "check_Dkc: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                //_customerCheckResult.value = false
            }
        }
    }

    fun updatePhone(phone: Phone) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.customerAPIService.updatePhone(phone)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                //_customerCheckResult.value = response
                Log.d("AccountViewModel", "check_Dkc: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                //_customerCheckResult.value = false
            }
        }
    }

    fun updateGender(gender: Gender) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.customerAPIService.updateGender(gender)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                //_customerCheckResult.value = response
                Log.d("AccountViewModel", "check_Dkc: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                //_customerCheckResult.value = false
            }
        }
    }

    fun updateBirthdate(birthdate: Birthdate) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.customerAPIService.updateBirthdate(birthdate)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                //_customerCheckResult.value = response
                Log.d("AccountViewModel", "check_Dkc: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                //_customerCheckResult.value = false
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