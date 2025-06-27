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

    var noticeUpdateResult by mutableStateOf("")
        private set

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