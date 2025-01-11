package com.example.ungdungbanthietbi_iot.data.slideshow

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SlideShowViewModel:ViewModel() {
    var listSlideShow:List<SlideShow> by mutableStateOf(emptyList())
        private set // Ngăn thay đổi trực tiếp từ bên ngoài
    fun getAllSlideShow(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                listSlideShow = RetrofitClient.slideshowAPIService.getAllSlideShow()
            }
            catch (e:Exception){
                Log.e("SlideShowViewModel", "Error getting slideshow", e)
            }
        }
    }
}