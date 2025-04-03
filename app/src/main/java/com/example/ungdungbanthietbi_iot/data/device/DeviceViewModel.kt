package com.example.ungdungbanthietbi_iot.data.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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

class DeviceViewModel:ViewModel() {

    var listAllDevice: List<Device> by mutableStateOf(emptyList())

    var listDeviceFeatured: List<Device> by mutableStateOf(emptyList())

    var device:Device by mutableStateOf(Device (0, "", "", "","", "", 0.0, 0, "", "", 0,0))

    var listDeviceOfCustomer by mutableStateOf<List<Device>>(emptyList())
        private set
    var listDeviceByOrder by mutableStateOf<List<Device>>(emptyList())


    private val _listDevce = MutableStateFlow<List<Device>>(emptyList())
    val listDevice: StateFlow<List<Device>> get() = _listDevce


    // Dữ liệu tìm kiếm
    var searchQuery: String by mutableStateOf("")
    var searchHistory: MutableList<String> by mutableStateOf(mutableStateListOf())
    var searchResult: List<Device> by mutableStateOf(emptyList())



    var deviceMap = mutableStateMapOf<String, Device>()
        private set

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
                // Kiểm tra dữ liệu trả về
                if (response.device.isNotEmpty()) {
                    listDeviceOfCustomer = response.device
                    Log.d("Device Success", "Lấy sản phẩm thành công: ${listDeviceOfCustomer.size} sản phẩm")
                } else {
                    listDeviceOfCustomer = emptyList()
                    Log.e("Device Error", "Không có sản phẩm trong danh sách yêu thích")
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
                listAllDevice = RetrofitClient.deviceAPIService.getAllDevice()
            } catch (e: Exception) {
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
                searchResult = listAllDevice // Hiển thị toàn bộ danh sách ban đầu
            }
            catch (e:Exception){
                Log.e("DeviceViewModel", "Error getting device", e)
            }
        }
    }
    // Tìm kiếm thiết bị và lưu vào lịch sử khi nhấn nút tìm kiếm
    fun searchDevice(query: String) {
        searchQuery = query
        if (query.isNotEmpty()) {
            val filteredDevices = listAllDevice.filter {
                it.name.contains(query.trim(), ignoreCase = true) || it.descriptionNormal.contains(query.trim(), ignoreCase = true)
            }
            searchResult = filteredDevices

            // Thêm từ khóa vào lịch sử tìm kiếm nếu chưa có
            if (!searchHistory.contains(query)) {
                searchHistory.add(query)
            }
        } else {
            // Khi không có từ khóa tìm kiếm, hiển thị tất cả thiết bị
            searchResult = listAllDevice
        }
    }

    // Xóa lịch sử tìm kiếm
    fun removeSearchHistory(keyword: String) {
        searchHistory.remove(keyword)
    }

    // Xóa toàn bộ lịch sử tìm kiếm
    fun clearSearchHistory() {
        searchHistory.clear()
    }

    fun getdeviceById2(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val device = RetrofitClient.deviceAPIService.getDeviceById(id)
                _listDevce.update { currentList -> currentList + device }
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Error getting Device", e)
            }
        }
    }
}