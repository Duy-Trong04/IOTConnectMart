package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.ResponseCustomer
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.AddCustomer
import com.example.ungdungbanthietbi_iot.models.Birthdate
import com.example.ungdungbanthietbi_iot.models.Customer
import com.example.ungdungbanthietbi_iot.models.Email
import com.example.ungdungbanthietbi_iot.models.Gender
import com.example.ungdungbanthietbi_iot.models.Phone
import com.example.ungdungbanthietbi_iot.models.Username
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class CustomerState {
    object Loading : CustomerState()
    data class Success(val customer: Customer) : CustomerState()
    data class Error(val message: String) : CustomerState()
}

data class UpdateCustomerUiState(
    val isLoading: Boolean = false,
    val customer: Customer? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class CustomerViewModel:ViewModel() {
    var customer by mutableStateOf<Customer?>(null)
        private set
    private val _customerState = MutableStateFlow<CustomerState>(CustomerState.Loading)
    val customerState: StateFlow<CustomerState> = _customerState

    private val _updateCustomerUiState = MutableStateFlow(UpdateCustomerUiState())
    val updateCustomerUiState: StateFlow<UpdateCustomerUiState> = _updateCustomerUiState.asStateFlow()

    var listCustomerReviewDevice by mutableStateOf<List<Customer>>(emptyList())

    private var customerAddResult by mutableStateOf("")

    private val _customerCheckResult = mutableStateOf<Boolean?>(null)
    val customerCheckResult: State<Boolean?> = _customerCheckResult

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            _updateCustomerUiState.value = UpdateCustomerUiState(isLoading = true)
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.customerAPIService.updateCustomer(customer)
                }
                if (response.statusCode == 200) {
                    _updateCustomerUiState.value = UpdateCustomerUiState(
                        isLoading = false,
                        customer = response.data,
                        error = null,
                        isSuccess = true
                    )
                    Log.d("CustomerViewModel", "Cập nhật khách hàng thành công: ${response.data}")
                } else {
                    _updateCustomerUiState.value = UpdateCustomerUiState(
                        isLoading = false,
                        customer = null,
                        error = "Cập nhật thất bại: Mã trạng thái ${response.statusCode}",
                        isSuccess = false
                    )
                    Log.e("CustomerViewModel", "Cập nhật thất bại: Mã trạng thái ${response.statusCode}")
                }
            } catch (e: Exception) {
                _updateCustomerUiState.value = UpdateCustomerUiState(
                    isLoading = false,
                    customer = null,
                    error = e.message ?: "Đã xảy ra lỗi khi cập nhật khách hàng",
                    isSuccess = false
                )
                Log.e("CustomerViewModel", "Lỗi khi cập nhật khách hàng: ${e.message}", e)
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

    fun getCustomerById9(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id.isBlank()) {
                _customerState.value = CustomerState.Error("Error: Customer ID cannot be empty")
                return@launch
            }
            _customerState.value = CustomerState.Loading
            try {
                val response = RetrofitClient.customerAPIService.getCustomerById9(id) // Correct method name
                if (response.statusCode == 200) {
                    _customerState.value = CustomerState.Success(response.data)
                } else {
                    _customerState.value = CustomerState.Error("Error: Status code ${response.statusCode} or invalid data")
                }
            } catch (e: HttpException) {
                _customerState.value = CustomerState.Error("HTTP Error: ${e.code()} - ${e.message()}")
            } catch (e: IOException) {
                _customerState.value = CustomerState.Error("Network Error: Please check your connection")
            } catch (e: Exception) {
                _customerState.value = CustomerState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }
    fun setErrorState(message: String) {
        _customerState.value = CustomerState.Error(message)
    }
    fun resetUpdateState() {
        _updateCustomerUiState.value = UpdateCustomerUiState()
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