package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.CustomerData
import com.example.ungdungbanthietbi_iot.api.deleteAddressRequest
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Address
import com.example.ungdungbanthietbi_iot.models.AddressBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressViewModel : ViewModel() {
    var listAddress by mutableStateOf<List<AddressBook>>(emptyList())


    var address by mutableStateOf<AddressBook?>(null)


    private var addressAddResult by mutableStateOf("")

    private var addressUpdateResult by mutableStateOf("")

    var addressDatas by mutableStateOf<CustomerData?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
    fun updateErrorMessage(message: String?) {
        errorMessage = message
    }

    fun getAddressById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AddressViewModel", "Bắt đầu lấy chi tiết địa chỉ với id: $id")
            isLoading = true
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
                    isLoading = false
                    Log.d("AddressViewModel", "Hoàn tất gọi API getAddressById. isLoading = false")
                }
            }
        }
    }
    fun getCustomerAddressBook(customerId: String) {
        viewModelScope.launch {
            Log.d("AddressViewModel", "Bắt đầu lấy danh sách địa chỉ cho customerId: $customerId")
            isLoading = true
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
                isLoading = false
                Log.d("AddressViewModel", "Hoàn tất gọi API getCustomerAddressBook. isLoading = false")
            }
        }
    }
    fun getAddressDefault(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AddressViewModel", "Bắt đầu lấy địa chỉ mặc định cho customerId: $customerId")
            isLoading = true
            try {
                Log.i("AddressViewModel", "Đang gọi API getCustomerAddressBook để tìm địa chỉ mặc định với customerId: $customerId")
                val response = RetrofitClient.addressAPIService.getCustomerAddressBook(customerId)
                withContext(Dispatchers.Main) {
                    if (response.status_code == 200 && response.data.data.address_books.isNotEmpty()) {
                        addressDatas = response.data.data
                        listAddress = response.data.data.address_books
                        val defaultAddress = response.data.data.address_books.find { it.is_default == 1 }
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
                    isLoading = false
                    Log.d("AddressViewModel", "Hoàn tất gọi API getCustomerAddressBook. isLoading = false")
                }
            }
        }
    }

//    fun getAddressByIdOrder(id: Int) {
//        viewModelScope.launch {
//            try {
//                val fetchedAddress = RetrofitClient.addressAPIService.getAddressByIdOrder(id)
//                address = fetchedAddress
//            } catch (e: Exception) {
//                Log.e("AddressViewModel", "Error getting Address", e)
//            }
//        }
//    }

//    fun getAddressByIdCustomer(idCustomer: String?) {
//        viewModelScope.launch {
//            try {
//                val response = withContext(Dispatchers.IO) {
//                    RetrofitClient.addressAPIService.getAddressByIdCustomer(idCustomer)
//                }
//                listAddress = response.address
//            } catch (e: Exception) {
//                Log.e("Address Error", "Lỗi khi lấy địa chỉ: ${e.message}")
//                listAddress = emptyList()
//            }
//        }
//    }

//    fun getAddressDefault(idCustomer: String?, isDefault: Int?) {
//        if (idCustomer == null || isDefault == null) {
//            Log.e("AddressViewModel", "Tham số idCustomer hoặc isDefault bị null")
//            return
//        }
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val fetchedAddress = RetrofitClient.addressAPIService.getAddressDefault(
//                    idCustomer = idCustomer,
//                    isDefault = isDefault
//                )
//                withContext(Dispatchers.Main) {
//                    address = fetchedAddress
//                }
//                Log.d("AddressViewModel", "Đã lấy địa chỉ thành công: $address")
//            } catch (e: Exception) {
//                Log.e("AddressViewModel", "Lỗi khi lấy địa chỉ mặc định", e)
//            }
//        }
//    }

//    fun addAddress(address: Address) {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitClient.addressAPIService.addAddress(address)
//                addressAddResult = if (response.success) {
//                    listAddress = listAddress + address
//                    "Thành công: ${response.message}"
//                } else {
//                    "Thất bại: ${response.message}"
//                }
//            } catch (e: Exception) {
//                Log.e("Add address", "Lỗi kết nối: ${e.message}")
//            }
//        }
//    }

//    fun updateAddress(address: Address) {
//        viewModelScope.launch {
//            try {
//                val response = withContext(Dispatchers.IO) {
//                    RetrofitClient.addressAPIService.updateAddress(address)
//                }
//                addressUpdateResult = if (response.success) {
//                    "Cập nhật thành công: ${response.message}"
//                } else {
//                    "Cập nhật thất bại: ${response.message}"
//                }
//            } catch (e: Exception) {
//                addressUpdateResult = "Lỗi khi cập nhật address: ${e.message}"
//                Log.e("Address Error", "Lỗi khi cập nhật address: ${e.message}")
//            }
//        }
//    }

//    fun deleteAddress(id: Int) {
//        viewModelScope.launch {
//            try {
//                val deleteRequest = deleteAddressRequest(id)
//                val response = RetrofitClient.addressAPIService.deleteAddress(deleteRequest)
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    if (apiResponse?.message == "Address Deleted") {
//                        listAddress = listAddress.filter { it.id != id }
//                        Log.d("AddressViewModel", "Address đã được xóa")
//                    } else {
//                        Log.e("AddressViewModel", "Lỗi: ${apiResponse?.message}")
//                    }
//                } else {
//                    Log.e("AddressViewModel", "Error: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("AddressViewModel", "Exception: ${e.message}")
//            }
//        }
//    }
}