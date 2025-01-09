package com.example.ungdungbanthietbi_iot.data.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceViewModel:ViewModel() {
    private val _devices = MutableLiveData<List<Device>>()
    val devices: LiveData<List<Device>> get() = _devices
    var listDevice: List<Device> by mutableStateOf(emptyList())
    private val _searchResult = MutableLiveData<List<Device>>()
    val searchResult: LiveData<List<Device>> get() = _searchResult
    var searchQuery: String by mutableStateOf("")
    var searchHistory: MutableList<String> = mutableListOf()

    init {
        getAllDevice()
    }

    fun getAllDevice(){
        viewModelScope.launch {
            try {
                val response = DeviceRetrofitClient.api.getAllDevice()
                _devices.postValue(response.results)
                _searchResult.postValue(response.results)  // Đưa dữ liệu vào _searchResult
            } catch (e: Exception) {
                e.printStackTrace() // Xử lý lỗi
            }
        }
    }

//    fun getDeviceByID(idDevice:String){
//        viewModelScope.launch (Dispatchers.IO){
//            try {
//                device = DeviceRetrofitClient.deviceAPIService.getDeviceByID(idDevice)
//                searchResult = listDevice // Hiển thị toàn bộ danh sách ban đầu
//            }
//            catch (e:Exception){
//                Log.e("DeviceViewModel", "Error getting device", e)
//            }
//        }
//    }
    // Tìm kiếm thiết bị và lưu vào lịch sử khi nhấn nút tìm kiếm
    fun searchDevice(query: String) {
        searchQuery = query
        if (query.isNotEmpty()) {
            val filteredDevices = _devices.value?.filter {
                it.name.contains(query.trim(), ignoreCase = true)
            } ?: emptyList()
            _searchResult.postValue(filteredDevices)

            // Thêm từ khóa vào lịch sử tìm kiếm nếu chưa có
            if (!searchHistory.contains(query)) {
                searchHistory.add(query)
            }
        } else {
            // Khi không có từ khóa tìm kiếm, hiển thị tất cả thiết bị
            _searchResult.postValue(_devices.value)
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