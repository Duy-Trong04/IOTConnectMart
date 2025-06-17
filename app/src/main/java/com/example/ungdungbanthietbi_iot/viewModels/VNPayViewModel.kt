package com.example.ungdungbanthietbi_iot.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.PaymentRequest
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

class VNPayViewModel: ViewModel() {
    private val _paymentUrl = MutableStateFlow<String?>(null)
    val paymentUrl: StateFlow<String?> = _paymentUrl

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun createPaymentUrl(paymentRequest: PaymentRequest, retries: Int = 3) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repeat(retries) { attempt ->
                try {
                    withTimeout(10000) { // Timeout 10 giây
                        val response = RetrofitClient.vnPayService.createPayment(paymentRequest)
                        _paymentUrl.value = response.paymentUrl
                        Log.d("VNPayViewModel", "URL Thanh toán: ${response.paymentUrl}")
                        return@withTimeout
                    }
                } catch (e: HttpException) {
                    Log.e("VNPayViewModel", "Lỗi HTTP lần $attempt: ${e.message()}")
                    if (attempt == retries - 1) {
                        _errorMessage.value = "Lỗi server: ${e.message()}"
                    }
                    kotlinx.coroutines.delay(1000)
                } catch (e: IOException) {
                    Log.e("VNPayViewModel", "Lỗi mạng lần $attempt: ${e.message}")
                    if (attempt == retries - 1) {
                        _errorMessage.value = "Lỗi mạng: ${e.message}"
                    }
                    kotlinx.coroutines.delay(1000)
                }
            }
            _isLoading.value = false
        }
    }

    fun clearPaymentUrl() {
        _paymentUrl.value = null
        Log.d("VNPayViewModel", "Đã xóa paymentUrl")
    }
}