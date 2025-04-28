package com.example.ungdungbanthietbi_iot.data.notice

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import com.example.ungdungbanthietbi_iot.data.address_book.Address
import com.example.ungdungbanthietbi_iot.data.order.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel : ViewModel(){

    var listNotice by mutableStateOf<List<Notice>>(emptyList())
        private set

    var noticeUpdateResult by mutableStateOf("")
        private set

    var noticeAddResult by mutableStateOf("")

    fun getNoticeByIdCustomer(idUser: String?) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.noticeAPIService.getNoticeByIdCustomer(idUser)
                }
                listNotice = response.notice ?: emptyList()
            } catch (e: Exception) {
                Log.e("Notice Error", "Lỗi khi lấy thông báo: ${e.message}")
                listNotice = emptyList()
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