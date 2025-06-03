package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Category
import com.example.ungdungbanthietbi_iot.models.SlideShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SlideShowViewModel:ViewModel() {
    private val _listSlideShows = MutableStateFlow<List<SlideShow>>(emptyList())
    // Public StateFlow for UI to observe
    val listSlideShows: StateFlow<List<SlideShow>> = _listSlideShows.asStateFlow()

    fun getAllSlideShow(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = RetrofitClient.slideshowAPIService.getAllSlideShow()
                _listSlideShows.value = response.data.data
            }
            catch (e:Exception){
                _listSlideShows.value = emptyList()
                Log.e("SlideShowViewModel", "Error getting slideshow", e)
            }
        }
    }
}