package com.example.ungdungbanthietbi_iot.data.account

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountViewModel:ViewModel() {
    var account: Account? by mutableStateOf(null)
        private set

    private val _loginResult = mutableStateOf<CheckLoginResponse?>(null)
    val loginResult: State<CheckLoginResponse?> = _loginResult

    var taikhoanUpdateResult by mutableStateOf("")

    var username: String? = null

    fun CheckLogin(username: String, password: String) {
        viewModelScope.launch {
            try {
                // Thực hiện yêu cầu API
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.accountAPIService.check_Login(username, password)
                }
                // Cập nhật kết quả API vào state
                _loginResult.value = response
            } catch (e: Exception) {
                // Xử lý lỗi nếu có
                Log.e("TaiKhoanViewModel", "Đã xảy ra lỗi: ${e.message}")
                _loginResult.value = CheckLoginResponse(result = false, message = e.message)
            }
        }
    }

    fun getUserByUsername(username: String) {
        this.username = username
        viewModelScope.launch(Dispatchers.IO) {
            try {
                account = RetrofitClient.accountAPIService.getAccountByUsername(username)
            } catch (e: Exception) {
                Log.e("SanPhamViewModel", "Error getting SanPham", e)
            }
        }
    }
}