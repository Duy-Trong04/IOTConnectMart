package com.example.ungdungbanthietbi_iot.viewModels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Device
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

class DeviceViewModel:ViewModel() {

    var listAllDevice: List<Device> by mutableStateOf(emptyList())

    var listDeviceFeatured: List<Device> by mutableStateOf(emptyList())

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> get() = _device

    private val _listDevice = MutableStateFlow<List<Device>>(emptyList())
    val listDevice: StateFlow<List<Device>> get() = _listDevice.asStateFlow()


    var deviceMap = mutableStateMapOf<Int, Device>()
        private set

    private val _listDeviceSearch = MutableStateFlow<List<Device>>(emptyList())
    val listDeviceSearch: StateFlow<List<Device>> get() = _listDeviceSearch
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery
    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> get() = _searchError
    fun getDeviceBySlug2(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.deviceAPIService.getDeviceById(id)
                Log.d("DeviceViewModel", "Lấy dữ liệu thiết bị: $response")
                if (response.statusCode == 200) {
                    val fetchedDevice = response.data.data.firstOrNull()
                    _device.value = fetchedDevice
                    fetchedDevice?.let { deviceMap[id] = it } // Lưu vào deviceMap nếu không null
                    Log.d("DeviceViewModel", "Đã lấy thiết bị: ${fetchedDevice?.name}")
                } else {
                    Log.e("DeviceViewModel", "Lấy thiết bị thất bại: $response")
                }
            } catch (e: Exception) {
                _device.value = null
                Log.e("DeviceViewModel", "Lỗi khi lấy thiết bị", e)
            }
        }
    }
    fun getAllDevice(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.deviceAPIService.getAllDevice()
                listAllDevice = response.data.data
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

    fun getDeviceBySlug(id: Int){
        viewModelScope.launch{
            try {
                val response = RetrofitClient.deviceAPIService.getDeviceById(id)
                Log.d("DeviceViewModel","Failed to fetch device: $response")
                if (response.statusCode == 200) {
                    _device.value = response.data.data.firstOrNull()
                    Log.d("DeviceViewModel","Fetched device: ${_device.value?.name}")
                } else {
                    Log.e("DeviceViewModel","Failed to fetch device: $response")
                }
            }
            catch (e:Exception){
                _device.value = null
                Log.e("DeviceViewModel", "Error getting device", e)
            }
        }
    }
    // Tìm kiếm thiết bị
    fun searchDevice(query: String) {
        viewModelScope.launch {
            try {
                val trimmedQuery = query.trim()
                _searchError.value = null // Reset lỗi

                if (trimmedQuery.isEmpty()) {
                    _listDeviceSearch.value = emptyList()
                    return@launch
                }
                // Xây dựng đối tượng filters JSON
                val filters = mutableMapOf<String, Any>()
                filters["logic"] = "OR"
                val filterList = mutableListOf<Map<String, String>>()
                if (trimmedQuery.isNotEmpty()) {
                    filterList.addAll(
                        listOf(
                            mapOf("field" to "product.name", "condition" to "contains", "value" to trimmedQuery),
                            mapOf("field" to "product.description", "condition" to "contains", "value" to trimmedQuery)
                        )
                    )
                }
                filters["filters"] = filterList

                // Chuyển filters thành chuỗi JSON và mã hóa URL
                val filtersJson = Gson().toJson(filters)
                Log.d("DeviceViewModel", "Filter JSON: $filtersJson")
                // Gọi API tìm kiếm
                val response = RetrofitClient.deviceAPIService.searchProducts(
                    page = 1,
                    limit = 12,
                    filters = filtersJson
                )

                if (response.statusCode == 200) {
                    _listDeviceSearch.value = response.data.data
                    Log.d("DeviceViewModel", "Tìm kiếm thành công: ${response.data.data.size} kết quả")
                } else {
                    _listDeviceSearch.value = emptyList()
                    _searchError.value = "Lỗi server: ${response.statusCode}"
                    Log.e("DeviceViewModel", "Tìm kiếm thất bại: ${response.statusCode}")
                }
            } catch (e: Exception) {
                _listDeviceSearch.value = emptyList()
                Log.e("DeviceViewModel", "Lỗi khi tìm kiếm", e)
            }
        }
    }

    // Cập nhật từ khóa tìm kiếm
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getDeviceCheckOut(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.deviceAPIService.getDeviceById(id)
                Log.d("DeviceViewModel", "Lấy thiết bị theo ID: Phản hồi = $response")
                if (response.statusCode == 200) {
                    val device = response.data.data.firstOrNull()
                    if (device != null) {
                        _listDevice.update { currentList ->
                            if (currentList.none { it.idDevice == device.idDevice }) {
                                currentList + device
                            } else {
                                currentList // Giữ danh sách hiện tại nếu thiết bị đã tồn tại
                            }
                        }
                        Log.d("DeviceViewModel", "Thêm thiết bị vào danh sách: ${device.name}")
                    } else {
                        Log.e("DeviceViewModel", "Không tìm thấy thiết bị trong phản hồi cho ID: $id")
                    }
                } else {
                    Log.e("DeviceViewModel", "Lỗi khi lấy thiết bị: StatusCode = ${response.statusCode}")
                }
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Lỗi khi lấy thiết bị theo ID", e)
            }
        }
    }
    // Hàm để xóa danh sách thiết bị
    fun clearDevices() {
        _listDevice.value = emptyList()
    }

    private fun decodeBase64ToBitmap(base64String: String?): Bitmap? {
        if (base64String.isNullOrEmpty()) {
            Log.d("DeviceViewModel", "Chuỗi Base64 rỗng hoặc null")
            return null
        }
        return try {
            val pureBase64 = if (base64String.contains(",")) base64String.split(",")[1] else base64String
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("DeviceViewModel", "Lỗi giải mã Base64: $base64String", e)
            null
        }
    }

    suspend fun getDeviceImageBitmap(device: Device): Bitmap? {
        return withContext(Dispatchers.IO) {
            decodeBase64ToBitmap(device.image)
        }
    }

    // Hàm để lấy danh sách bitmap cho tất cả hình ảnh trong device.images
    suspend fun getDeviceImagesBitmap(device: Device): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            device.images.mapNotNull { image ->
                decodeBase64ToBitmap(image.image)
            }
        }
    }
}