package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.deleteAddressRequest
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressViewModel : ViewModel() {
    var listAddress by mutableStateOf<List<Address>>(emptyList())
        private set

    var address by mutableStateOf<Address?>(null)
        private set

    private var addressAddResult by mutableStateOf("")

    private var addressUpdateResult by mutableStateOf("")

    fun getAddressById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedAddress = RetrofitClient.addressAPIService.getAddressById(id)
                withContext(Dispatchers.Main) {
                    address = fetchedAddress
                }
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error getting Address", e)
            }
        }
    }

    fun getAddressByIdOrder(id: Int) {
        viewModelScope.launch {
            try {
                val fetchedAddress = RetrofitClient.addressAPIService.getAddressByIdOrder(id)
                address = fetchedAddress
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error getting Address", e)
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
                Log.e("Address Error", "Lỗi khi lấy địa chỉ: ${e.message}")
                listAddress = emptyList()
            }
        }
    }

    fun getAddressDefault(idCustomer: String?, isDefault: Int?) {
        if (idCustomer == null || isDefault == null) {
            Log.e("AddressViewModel", "Tham số idCustomer hoặc isDefault bị null")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedAddress = RetrofitClient.addressAPIService.getAddressDefault(
                    idCustomer = idCustomer,
                    isDefault = isDefault
                )
                withContext(Dispatchers.Main) {
                    address = fetchedAddress
                }
                Log.d("AddressViewModel", "Đã lấy địa chỉ thành công: $address")
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Lỗi khi lấy địa chỉ mặc định", e)
            }
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.addressAPIService.addAddress(address)
                addressAddResult = if (response.success) {
                    listAddress = listAddress + address
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

    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = deleteAddressRequest(id)
                val response = RetrofitClient.addressAPIService.deleteAddress(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Address Deleted") {
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