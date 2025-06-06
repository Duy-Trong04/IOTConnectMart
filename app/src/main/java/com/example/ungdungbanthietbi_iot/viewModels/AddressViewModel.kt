package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.CreateAddressRequest
import com.example.ungdungbanthietbi_iot.api.CustomerData
import com.example.ungdungbanthietbi_iot.api.UpdateAddressRequest
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.AddressBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressViewModel : ViewModel() {
    var listAddress by mutableStateOf<List<AddressBook>>(emptyList())
        private set

    var address by mutableStateOf<AddressBook?>(null)
        private set

    var addressDatas by mutableStateOf<CustomerData?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    fun updateErrorMessage(message: String?) {
        errorMessage = message
    }

    fun getAddressById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AddressViewModel", "Bắt đầu lấy chi tiết địa chỉ với id: $id")
            _isLoading.value = true
            try {
                Log.i("AddressViewModel", "Đang gọi API getAddressById với id: $id")
                val response = RetrofitClient.addressAPIService.getAddressById(id)
                withContext(Dispatchers.Main) {
                    if (response.status_code == 200) {
                        address = response.data
                        errorMessage = null
                        Log.i("AddressViewModel", "Lấy chi tiết địa chỉ thành công: ${response.data}")
                    } else {
                        errorMessage = "Lấy dữ liệu thất bại: Mã trạng thái ${response.status_code}"
                        Log.w("AddressViewModel", "API trả về mã trạng thái không thành công: ${response.status_code}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Lỗi khi gọi API: ${e.message}"
                    Log.e("AddressViewModel", "Lỗi khi lấy chi tiết địa chỉ: ${e.message}", e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    Log.d("AddressViewModel", "Hoàn tất gọi API getAddressById. isLoading = false")
                }
            }
        }
    }
    fun getCustomerAddressBook(customerId: String) {
        viewModelScope.launch {
            Log.d("AddressViewModel", "Bắt đầu lấy danh sách địa chỉ cho customerId: $customerId")
            _isLoading.value = true
            try {
                Log.i("AddressViewModel", "Đang gọi API getCustomerAddressBook với customerId: $customerId")
                val response = RetrofitClient.addressAPIService.getCustomerAddressBook(customerId)
                if (response.status_code == 200) {
                    addressDatas = response.data.data
                    errorMessage = null
                    Log.i("AddressViewModel", "Lấy địa chỉ thành công. Số lượng địa chỉ: ${addressDatas!!.address_books.size}")
                } else {
                    errorMessage = "Lấy dữ liệu thất bại: Mã trạng thái ${response.status_code}"
                    Log.w("AddressViewModel", "API trả về mã trạng thái không thành công: ${response.status_code}")
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi khi gọi API: ${e.message}"
                Log.e("AddressViewModel", "Lỗi khi lấy địa chỉ: ${e.message}", e)
            } finally {
                _isLoading.value = false
                Log.d("AddressViewModel", "Hoàn tất gọi API getCustomerAddressBook. isLoading = false")
            }
        }
    }
    fun getAddressDefault(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AddressViewModel", "Bắt đầu lấy địa chỉ mặc định cho customerId: $customerId")
            _isLoading.value = true
            try {
                Log.i("AddressViewModel", "Đang gọi API getCustomerAddressBook để tìm địa chỉ mặc định với customerId: $customerId")
                val response = RetrofitClient.addressAPIService.getCustomerAddressBook(customerId)
                withContext(Dispatchers.Main) {
                    if (response.status_code == 200 && response.data.data.address_books.isNotEmpty()) {
                        addressDatas = response.data.data
                        listAddress = response.data.data.address_books
                        val defaultAddress = response.data.data.address_books.find { it.is_default }
                        if (defaultAddress != null) {
                            address = defaultAddress
                            errorMessage = null
                            Log.i("AddressViewModel", "Tìm thấy địa chỉ mặc định: ${defaultAddress.receiver_name}")
                        } else {
                            address = null
                            errorMessage = "Không tìm thấy địa chỉ mặc định cho customerId: $customerId"
                            Log.w("AddressViewModel", "Không có địa chỉ mặc định trong danh sách địa chỉ")
                        }
                    } else {
                        address = null
                        errorMessage = if (response.data.data.address_books.isEmpty()) {
                            "Danh sách địa chỉ rỗng cho customerId: $customerId"
                        } else {
                            "Lấy dữ liệu thất bại: Mã trạng thái ${response.status_code}"
                        }
                        Log.w("AddressViewModel", "API trả về mã trạng thái không thành công: ${response.status_code}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    address = null
                    errorMessage = "Lỗi khi gọi API: ${e.message}"
                    Log.e("AddressViewModel", "Lỗi khi lấy địa chỉ mặc định: ${e.message}", e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    Log.d("AddressViewModel", "Hoàn tất gọi API getCustomerAddressBook. isLoading = false")
                }
            }
        }
    }
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun createAddress(request: CreateAddressRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.addressAPIService.createAddress(request)
                if(response.status_code == 201){
                    _error.value = null
                }
                else{
                    _error.value = "Failed to update review, status: ${response.status_code}"
                    Log.e("AddressViewModel", "Failed to add address, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _error.value = "Error creating address: ${e.message}"
                Log.e("Add Address", "Lỗi kết nối: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAddress(request: UpdateAddressRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.addressAPIService.updateAddress(request)

                if(response.status_code == 200){
                    _error.value = null
                }
                else{
                    _error.value = "Failed to update address, status: ${response.status_code}"
                    Log.e("AddressViewModel", "Failed to update address, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _error.value = "Error updating address: ${e.message}"
                Log.e("Add Address", "Lỗi kết nối: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAddress(customerId: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.addressAPIService.deleteAddress(customerId, id)
                if(response.status_code == 200){
                    _error.value = null
                }
                else{
                    _error.value = "Failed to update address, status: ${response.status_code}"
                    Log.e("AddressViewModel", "Failed to update address, status: ${response.status_code}")
                }
            } catch (e: Exception) {
                _error.value = "Error updating address: ${e.message}"
                Log.e("Add Address", "Lỗi kết nối: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }
}