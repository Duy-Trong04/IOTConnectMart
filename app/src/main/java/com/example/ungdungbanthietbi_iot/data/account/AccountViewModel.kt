package com.example.ungdungbanthietbi_iot.data.account

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel : ViewModel() {
    var taiKhoan: Account? by mutableStateOf(null)
        private set

    var taikhoanUpdateResult by mutableStateOf("")

    fun checkLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitClient.apiService.KiemTraLogin(username, password)
                // Đảm bảo là sau khi có dữ liệu mới cập nhật taiKhoan
                withContext(Dispatchers.Main) {
                    Log.d("TaiKhoanViewModel", "Dữ liệu trả về checklogin: $result")
                    taiKhoan = result
                }
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Lỗi khi lấy dữ liệu từ API", e)
                withContext(Dispatchers.Main) {
                    taiKhoan = null // Xử lý lỗi bằng cách gán giá trị null
                }
            }
        }
    }
}

class GetTaiKhoan: ViewModel() {
    var taiKhoan1: Account? by mutableStateOf(null)
        private set

    fun getTaiKhoanByTentaikhoan(username: String) {
            viewModelScope.launch() {
                try {
                    Log.d("TaiKhoanViewModel", "Gửi yêu cầu đến API với tên tài khoản: $username")
                    taiKhoan1 = RetrofitClient.apiService.getTaiKhoanByTentaikhoan(username)
                    Log.d("TaiKhoanViewModel", "Dữ liệu trả về getTaiKhoanByTentaikhoan: $taiKhoan1")
                } catch (e: Exception) {
                    Log.d("SanPhamViewModel", "Error getting SanPham")
                }
            }
    }
}