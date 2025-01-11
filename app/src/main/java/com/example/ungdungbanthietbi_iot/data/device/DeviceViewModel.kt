package com.example.ungdungbanthietbi_iot.data.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceViewModel:ViewModel() {

    var listAllDevice: List<Device> by mutableStateOf(emptyList())
    var listDeviceFeatured: List<Device> by mutableStateOf(emptyList())
    var device:Device by mutableStateOf(Device (0, "", "", "","", "", 0.0, 0, "", "", 0,0))


    // Dữ liệu tìm kiếm
    var searchQuery: String by mutableStateOf("")
    var searchHistory: MutableList<String> by mutableStateOf(mutableStateListOf())
    var searchResult: List<Device> by mutableStateOf(emptyList())

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

    fun getDeviceBySlug(slug:String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                device = RetrofitClient.deviceAPIService.getDeviceBySlug(slug)
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
}