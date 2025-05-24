package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeviceViewModel:ViewModel() {

    var listAllDevice: List<Device> by mutableStateOf(emptyList())

    var listDeviceFeatured: List<Device> by mutableStateOf(emptyList())

    var device: Device by mutableStateOf(Device (0, "", "", "","", "", 0.0, 0, "", "", 0,0))

    var listDeviceOfCustomer by mutableStateOf<List<Device>>(emptyList())
        private set


    var listDeviceByOrder by mutableStateOf<List<Device>>(emptyList())


    private val _listDevice = MutableStateFlow<List<Device>>(emptyList())
    val listDevice: StateFlow<List<Device>> get() = _listDevice


    var deviceMap = mutableStateMapOf<String, Device>()
        private set

    private val _listDeviceSearch = MutableStateFlow<List<Device>>(emptyList())
    val listDeviceSearch: StateFlow<List<Device>> get() = _listDeviceSearch
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    fun getDeviceBySlug2(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedDevice = RetrofitClient.deviceAPIService.getDeviceById(id)
                deviceMap[id] = fetchedDevice  // Lưu riêng từng sản phẩm
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Error getting device", e)
            }
        }
    }
    fun getDeviceByCart(idCustomer: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.deviceAPIService.getDeviceByCart(idCustomer)
                }
                listDeviceOfCustomer = response.device
            } catch (e: Exception) {
                Log.e("Device Error", "Lỗi khi lấy device: ${e.message}")
            }
        }
    }
    // Map lưu trữ danh sách thiết bị theo từng orderId
    var devicesByOrder by mutableStateOf<Map<Int, List<Device>>>(emptyMap())
        private set

    fun getDeviceByIdOrder2(orderId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.deviceAPIService.getDeviceByIdOrder(orderId)
                }
                // Cập nhật vào map
                devicesByOrder = devicesByOrder.toMutableMap().apply {
                    put(orderId, response.device)
                }
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Lỗi khi lấy thiết bị: ${e.message}")
            }
        }
    }
    fun getDeviceByIdOrder(id: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.deviceAPIService.getDeviceByIdOrder(id)
                }
                listDeviceByOrder = response.device
            } catch (e: Exception) {
                Log.e("Device Error", "Lỗi khi lấy Device")
            }
        }
    }
    fun getDeviceByLiked(idCustomer: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.deviceAPIService.getDeviceByLiked(idCustomer)
                }
                // Giả sử API trả về một trường như `devices` hoặc `data`
                val devices = response.device // Điều chỉnh dựa trên cấu trúc API
                if (devices.isNotEmpty()) {
                    listDeviceOfCustomer = devices
                    Log.d("Device Success", "Lấy sản phẩm thành công: ${listDeviceOfCustomer.size} sản phẩm - Dữ liệu: $listDeviceOfCustomer")
                } else {
                    listDeviceOfCustomer = emptyList()
                    Log.w("Device Warning", "Không có sản phẩm trong danh sách yêu thích")
                }
            } catch (e: Exception) {
                Log.e("Device Error", "Lỗi khi lấy sản phẩm: ${e.message}")
                listDeviceOfCustomer = emptyList() // Đảm bảo danh sách không null
            }
        }
    }
    fun getAllDevice(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.deviceAPIService.getAllDevice()
                listAllDevice = response
            } catch (e: Exception) {
                listAllDevice = emptyList()
                e.printStackTrace() // Xử lý lỗi
            }
        }
    }

    fun getDeviceFeatured(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listDeviceFeatured = RetrofitClient.deviceAPIService.getDeviceFeatured()
            } catch (e: Exception) {
                e.printStackTrace() // Xử lý lỗi
            }
        }
    }

    fun getDeviceBySlug(id:String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                device = RetrofitClient.deviceAPIService.getDeviceById(id)
            }
            catch (e:Exception){
                Log.e("DeviceViewModel", "Error getting device", e)
            }
        }
    }
    // Tìm kiếm thiết bị
    fun searchDevice(name: String, des: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.deviceAPIService.searchDevice(name, des)
                }
                if (response.device.isNotEmpty()) {
                    _listDeviceSearch.value = response.device
                    Log.d("Search Success", "Tìm kiếm thành công: ${response.device.size} thiết bị")
                } else {
                    _listDeviceSearch.value = emptyList()
                    Log.e("Search Error", "Không tìm thấy thiết bị phù hợp")
                }
            } catch (e: Exception) {
                Log.e("Search Error", "Lỗi khi tìm kiếm thiết bị: ${e.message}")
                _listDeviceSearch.value = emptyList()
            }
        }
    }

    // Cập nhật từ khóa tìm kiếm
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getdeviceById2(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val device = RetrofitClient.deviceAPIService.getDeviceById(id)
                _listDevice.update { currentList ->
                    // Chỉ thêm thiết bị nếu chưa tồn tại
                    if (currentList.none { it.idDevice == device.idDevice }) {
                        currentList + device
                    } else {
                        currentList // Giữ nguyên danh sách nếu thiết bị đã tồn tại
                    }
                }
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Error getting Device", e)
            }
        }
    }
    // Hàm để xóa danh sách thiết bị
    fun clearDevices() {
        _listDevice.value = emptyList()
    }
}