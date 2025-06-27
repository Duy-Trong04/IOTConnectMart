package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.AddressBook
import com.example.ungdungbanthietbi_iot.models.Category
import com.example.ungdungbanthietbi_iot.models.SlideShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SlideShowViewModel:ViewModel() {
    private val _listSlideShows = MutableStateFlow<List<SlideShow>>(emptyList())
    // Public StateFlow for UI to observe
    val listSlideShows: StateFlow<List<SlideShow>> = _listSlideShows.asStateFlow()
    var slideshow by mutableStateOf<SlideShow?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingAll = MutableStateFlow(false)
    val isLoadingAll: StateFlow<Boolean> = _isLoadingAll.asStateFlow()

    fun getSlideShowById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SlideShowViewModel", "Bắt đầu lấy chi tiết slideshow với id: $id")
            _isLoading.value = true
            try {
                Log.i("SlideShowViewModel", "Đang gọi API getAddressById với id: $id")
                val response = RetrofitClient.slideshowAPIService.getSlideShowById(id)
                withContext(Dispatchers.Main) {
                    if (response.status_code == 200) {
                        slideshow = response.data
                        errorMessage = null
                        Log.i("SlideShowViewModel", "Lấy chi tiết slideshow thành công: ${response.data}")
                    } else {
                        errorMessage = "Lấy dữ liệu thất bại: Mã trạng thái ${response.status_code}"
                        Log.w("SlideShowViewModel", "API trả về mã trạng thái không thành công: ${response.status_code}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Lỗi khi gọi API: ${e.message}"
                    Log.e("SlideShowViewModel", "Lỗi khi lấy chi tiết slideshow: ${e.message}", e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    Log.d("SlideShowViewModel", "Hoàn tất gọi API getSlideShowById. isLoading = false")
                }
            }
        }
    }
    fun getAllSlideShow(){
        viewModelScope.launch(Dispatchers.IO){
            _isLoadingAll.value = true
            try{
                val response = RetrofitClient.slideshowAPIService.getAllSlideShow()
                _listSlideShows.value = response.data.data
            }
            catch (e:Exception){
                _listSlideShows.value = emptyList()
                Log.e("SlideShowViewModel", "Error getting slideshow", e)
            }
            finally {
                _isLoadingAll.value = false
            }
        }
    }
}