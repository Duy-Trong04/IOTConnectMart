package com.example.ungdungbanthietbi_iot.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.SendTokenRequest
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.dataStore
import com.example.ungdungbanthietbi_iot.models.Notice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel : ViewModel(){

    private val _listNotice = MutableStateFlow<List<Notice>>(emptyList())
    val listNotice: StateFlow<List<Notice>> = _listNotice.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingRead = MutableStateFlow(false)
    val isLoadingRead: StateFlow<Boolean> = _isLoadingRead.asStateFlow()

    fun getNotifications(token: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val authToken = "Bearer $token"
                val response = RetrofitClient.noticeAPIServiceEcom.getNotifications(authToken, type)
                _listNotice.value = response.data
                Log.d("NoticeViewModel","Fetched all notice: ${response.data.size}")
            } catch (e: Exception) {
                _listNotice.value = emptyList()
                Log.e("NoticeViewModel","Failed to fetch all notice: $e")
                e.printStackTrace() // Xử lý lỗi
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun readNotification(token: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoadingRead.value = true
            try {
                val authToken = "Bearer $token"
                val response = RetrofitClient.noticeAPIServiceEcom.readNotification(authToken, id)
                if (response.isSuccessful) {
                    Log.i("NoticeViewModel", "Đã đọc thông báo thành công")
                } else {
                    Log.e("NoticeViewModel", "Đọc thông báo thất bại: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("NoticeViewModel", "Lỗi đọc thông báo: ${e.message}")
            } finally {
                _isLoadingRead.value = false
            }
        }
    }

    fun sendTokenToServer(token: SendTokenRequest, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val preferences = context.dataStore.data.first()
                val accessToken = preferences[stringPreferencesKey("access_token")] ?: ""
                Log.d("Token User", accessToken)
                val authHeader = "Bearer $accessToken"
                Log.d("Token User", authHeader)
                val response = RetrofitClient.noticeAPIService.sendTokenDevice(authHeader, token )
                if (response.isSuccessful) {
                    Log.i("FCM Token", "Token sent successfully")
                } else {
                    Log.e("FCM Token", "Failed to send token: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("FCM Token", "Error sending token: ${e.message}")
            }
        }
    }

    fun sendToken(token: SendTokenRequest, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val preferences = context.dataStore.data.first()
                val accessToken = preferences[stringPreferencesKey("access_token")] ?: ""
                Log.d("Token User", accessToken)
                val authHeader = "Bearer $accessToken"
                Log.d("Token User", authHeader)
                val response = RetrofitClient.noticeAPIServiceEcom.sendToken(authHeader, token )
                if (response.isSuccessful) {
                    Log.i("FCM Token", "Token sent successfully")
                } else {
                    Log.e("FCM Token", "Failed to send token: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("FCM Token", "Error sending token: ${e.message}")
            }
        }
    }
}