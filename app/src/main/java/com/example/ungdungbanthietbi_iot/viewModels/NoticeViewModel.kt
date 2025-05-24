package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.models.Notice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel : ViewModel(){

    private val _listNotice = MutableStateFlow<List<Notice>>(emptyList())
    val listNotice: StateFlow<List<Notice>> = _listNotice.asStateFlow()

    var noticeUpdateResult by mutableStateOf("")
        private set

    var noticeAddResult by mutableStateOf("")

    fun getNoticeByIdCustomer(idUser: String?) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.noticeAPIService.getNoticeByIdCustomer(idUser)
                }
                _listNotice.value = response.notice ?: emptyList()
            } catch (e: Exception) {
                Log.e("Notice Error", "Lỗi khi lấy thông báo: ${e.message}")
                _listNotice.value = emptyList()
            }
        }
    }

    fun updateNotice(notice: Notice) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.noticeAPIService.updateNotice(notice)
                }
                noticeUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                noticeUpdateResult = "Lỗi khi cập nhật notice: ${e.message}"
                Log.e("Notice Error", "Lỗi khi cập nhật notice: ${e.message}")
            }
        }
    }
    fun addNotice(notice: Notice) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.noticeAPIService.addNotice(notice)
                noticeAddResult = if (response.success) {
                    "Thành công: ${response.message}"
                } else {
                    "Thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("Add Notice", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}