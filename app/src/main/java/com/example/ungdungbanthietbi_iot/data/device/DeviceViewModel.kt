package com.example.ungdungbanthietbi_iot.data.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceViewModel:ViewModel() {
    var listDevice: List<Device> by mutableStateOf(emptyList())
    var device:Device by mutableStateOf(Device (0, "", "", "","", "", 0.0, 0, "", "", 0,0))
    var searchResult: List<Device> by mutableStateOf(emptyList())
    var searchQuery: String by mutableStateOf("")
    var searchHistory: MutableList<String> = mutableStateListOf()  // Sử dụng MutableList để có thể thay đổi
    fun getAllDevice(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                listDevice = DeviceRetrofitClient.deviceAPIService.getAllDevice()
            }
            catch (e:Exception){
                Log.e("DeviceViewModel", "Error getting device", e)
            }
        }
    }

    fun getDeviceByID(idDevice:String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                device = DeviceRetrofitClient.deviceAPIService.getDeviceByID(idDevice)
                searchResult = listDevice // Hiển thị toàn bộ danh sách ban đầu
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
            // Thực hiện tìm kiếm thiết bị
            searchResult = listDevice.filter { it.name.contains(query.trim(), ignoreCase = true) }

            // Thêm từ khóa vào lịch sử chỉ khi tìm kiếm
            if (!searchHistory.contains(query)) {
                searchHistory.add(query)
            }
        } else {
            searchResult = listDevice // Hiển thị tất cả thiết bị khi không có từ khóa tìm kiếm
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