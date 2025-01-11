package com.example.ungdungbanthietbi_iot.data.image_device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel:ViewModel() {
    var listImageByIdDevice by mutableStateOf<List<Image>>(emptyList())
    var listImage: List<Image> by mutableStateOf(emptyList())

    fun getImageByIdDevice(idDevice:String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                listImage = RetrofitClient.imageAPIService.getImageByIdDevice(idDevice)
            }
            catch (e:Exception){
                Log.e("DeviceViewModel", "Error getting image", e)
            }
        }
    }
}