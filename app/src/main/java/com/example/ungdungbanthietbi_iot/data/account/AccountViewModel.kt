package com.example.ungdungbanthietbi_iot.data.account

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.data.RetrofitClient
import com.example.ungdungbanthietbi_iot.data.cart.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountViewModel:ViewModel() {
    var account: Account? by mutableStateOf(null)
        private set

    var accountAddResult by mutableStateOf("")

    private val _loginResult = mutableStateOf<CheckLoginResponse?>(null)
    val loginResult: State<CheckLoginResponse?> = _loginResult

    var taikhoanUpdateResult by mutableStateOf("")

    var username: String? = null

    private val _accountCheckResult = mutableStateOf(true)
    val accountCheckResult: State<Boolean> = _accountCheckResult


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

    //Them vào account
    fun addToAccount(account: AddAccount) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm account trên server
                val response = RetrofitClient.accountAPIService.addAccount(account)
                accountAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddToAccount", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun check_Dk(account: AddAccount) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.accountAPIService.checkAccount_Dk(account)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                withContext(Dispatchers.Main) {
                    _accountCheckResult.value = response.result
                }
                Log.d("AccountViewModel", "check_Dka: ${response.result}")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                _accountCheckResult.value = false
            }
        }
    }
}