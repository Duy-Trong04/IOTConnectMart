package com.example.ungdungbanthietbi_iot.data.address_book

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressViewModel: ViewModel() {
    var listAddress by mutableStateOf<List<Address>>(emptyList())

    var address by mutableStateOf<Address?>(null)
        private set

    var addressAddResult by mutableStateOf("")
    var addressUpdateResult by mutableStateOf("")

    private val _danhsachDiaChi = MutableStateFlow<List<Address>>(emptyList())
    val danhsachDiaChi: StateFlow<List<Address>> get() = _danhsachDiaChi


    fun getAddressById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                address = RetrofitClient.addressAPIService.getAddressById(id)
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error getting Address", e)
            }
        }
    }

    fun getAddressById2(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val address = RetrofitClient.addressAPIService.getAddressById(id)
                _danhsachDiaChi.update { currentList -> currentList + address }
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error getting SanPham", e)
            }
        }
    }
    fun getAddressByIdCustomer(idCustomer: String?) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.addressAPIService.getAddressByIdCustomer(idCustomer)
                }
                listAddress = response.address
            } catch (e: Exception) {
                Log.e("Addrss Error", "Lỗi khi lấy dia chi: ${e.message}")
            }
        }
    }

    fun getAddressDefault(idCustomer: String?, isDefault: Int?) {
        if (idCustomer == null || isDefault == null) {
            Log.e("AddressViewModel", "Tham số idCustomer hoặc isDefault bị null")
            return // Ngừng xử lý nếu tham số null
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                address = RetrofitClient.addressAPIService.getAddressDefault(
                    idCustomer = idCustomer,
                    isDefault = isDefault
                )
                Log.d("AddressViewModel", "Đã lấy địa chỉ thành công: $address")
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Lỗi khi lấy địa chỉ mặc định", e)
            }
        }
    }

    fun addAddress(address:Address) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = RetrofitClient.addressAPIService.addAddress(address)
                addressAddResult = if (response.success) {
                    "Thành công: ${response.message}"
                } else {
                    "Thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("Add address", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.addressAPIService.updateAddress(address)
                }
                addressUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                addressUpdateResult = "Lỗi khi cập nhật address: ${e.message}"
                Log.e("Address Error", "Lỗi khi cập nhật address: ${e.message}")
            }
        }
    }

    fun updateAddressDefault(idCustomer: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.addressAPIService.updateAddressDefault(idCustomer)
                }
                addressUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                addressUpdateResult = "Lỗi khi cập nhật Address: ${e.message}"
                Log.e("Address Error", "Lỗi khi cập nhật Address: ${e.message}")
            }
        }
    }
    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = deleteAddressRequest(id)
                val response = RetrofitClient.addressAPIService.deleteAddress(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Address Deleted") {
                        // Cập nhật lại giỏ hàng trong ViewModel
                        listAddress = listAddress.filter { it.id != id }
                        Log.d("AddressViewModel", "Address đã được xóa")
                    } else {
                        Log.e("AddressViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("AddressViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Exception: ${e.message}")
            }
        }
    }

}