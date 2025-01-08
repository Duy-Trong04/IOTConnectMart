package com.example.ungdungbanthietbi_iot.data.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceViewModel:ViewModel() {
    var listDevice: List<Device> by mutableStateOf(emptyList())
    var device:Device by mutableStateOf(Device (0, "", "", "","", "", 0.0, 0, "", "", 0,0))
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
            }
            catch (e:Exception){
                Log.e("DeviceViewModel", "Error getting device", e)
            }
        }
    }

}