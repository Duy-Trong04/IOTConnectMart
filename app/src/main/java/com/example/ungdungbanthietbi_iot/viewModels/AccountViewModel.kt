package com.example.ungdungbanthietbi_iot.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.ChangePasswordRequest
import com.example.ungdungbanthietbi_iot.api.ChangePasswordResponse
import com.example.ungdungbanthietbi_iot.api.ChangePasswordUiState
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.api.RegisterRequest
import com.example.ungdungbanthietbi_iot.api.RegisterResponse
import com.example.ungdungbanthietbi_iot.api.ResetPasswordRequest
import com.example.ungdungbanthietbi_iot.api.ResetPasswordResponse
import com.example.ungdungbanthietbi_iot.api.SendOtpRequest
import com.example.ungdungbanthietbi_iot.api.SendOtpResponse
import com.example.ungdungbanthietbi_iot.api.VerifyOtpChangeEmailRequest
import com.example.ungdungbanthietbi_iot.api.VerifyOtpRequest
import com.example.ungdungbanthietbi_iot.api.VerifyOtpResponse
import com.example.ungdungbanthietbi_iot.dataStore
import com.example.ungdungbanthietbi_iot.models.Account
import com.example.ungdungbanthietbi_iot.models.LoginRequest
import com.example.ungdungbanthietbi_iot.models.LoginResponse
import com.example.ungdungbanthietbi_iot.models.UpdatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AccountViewModel:ViewModel() {
    var account: Account? by mutableStateOf(null)
        private set
    var username: String? = null

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    private val _accountCheckResult = mutableStateOf<Boolean?>(null)
    val accountCheckResult: State<Boolean?> = _accountCheckResult

    suspend fun logout(context: Context) {
        try {
            // Xóa toàn bộ dữ liệu trong DataStore
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
            // Đặt lại trạng thái loginUiState
            _loginUiState.value = LoginUiState(
                isLoading = false,
                accessToken = null,
                customer_id = null,
                error = null,
                result = false
            )
            // Ghi log để debug
            Log.d("AccountViewModel", "Đăng xuất thành công")
        } catch (e: Exception) {
            _loginUiState.value = LoginUiState(
                isLoading = false,
                accessToken = null,
                customer_id = null,
                error = "Lỗi khi đăng xuất: ${e.message}",
                result = false
            )
            Log.e("AccountViewModel", "Lỗi khi đăng xuất: ${e.message}", e)
        }
    }


    private val _registerResult = MutableStateFlow<Response<RegisterResponse>?>(null)
    val registerResult: StateFlow<Response<RegisterResponse>?> = _registerResult


    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.accountAPIService.addAccount(request)
                _registerResult.value = response
                if (response.isSuccessful) {
                    Log.d("AccountViewModel", "Registration successful: ${response.body()?.data}")
                } else {
                    Log.e("AccountViewModel", "Registration failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Registration error: ${e.message}")
                _registerResult.value = null
            }
        }
    }

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun changePassword(username: String, password: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value = ChangePasswordUiState(isLoading = true)
            try {
                val request = ChangePasswordRequest(
                    username = username,
                    password = password,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
                val response: Response<ChangePasswordResponse> = RetrofitClient.accountAPIService.changePassword(request)
                if (response.isSuccessful && response.body()?.status_code == 200) {
                    _uiState.value = ChangePasswordUiState(
                        isLoading = false,
                        statusCode = response.body()?.status_code,
                        error = null,
                        result = true
                    )
                } else {
                    _uiState.value = ChangePasswordUiState(
                        isLoading = false,
                        statusCode = response.body()?.status_code,
                        error = "Đổi mật khẩu thất bại: Mã trạng thái ${response.code()}",
                        result = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ChangePasswordUiState(
                    isLoading = false,
                    statusCode = null,
                    error = e.message ?: "Đã xảy ra lỗi khi đổi mật khẩu",
                    result = false
                )
            }
        }
    }



    fun checkLogin(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)
            try {
                val request = LoginRequest(
                    username = username,
                    password = password,
                    type = "CUSTOMER"
                )
                val response: LoginResponse = RetrofitClient.accountAPIService.login(request)
                if (response.status_code == 200) {
                    _loginUiState.value = LoginUiState(
                        isLoading = false,
                        accessToken = response.data.accessToken,
                        customer_id = response.data.data.customer_id,
                        error = null,
                        result = true
                    )
                } else {
                    _loginUiState.value = LoginUiState(
                        isLoading = false,
                        accessToken = null,
                        customer_id = null,
                        error = "Đăng nhập thất bại: Mã trạng thái ${response.status_code}",
                        result = false
                    )
                }
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState(
                    isLoading = false,
                    accessToken = null,
                    customer_id = null,
                    error = e.message ?: "Đã xảy ra lỗi khi đăng nhập",
                    result = false
                )
            }
        }
    }


    private val _sendOtpResult = MutableStateFlow<Response<SendOtpResponse>?>(null)
    val sendOtpResult: StateFlow<Response<SendOtpResponse>?> = _sendOtpResult

    private val _verifyOtpResult = MutableStateFlow<Response<VerifyOtpResponse>?>(null)
    val verifyOtpResult: StateFlow<Response<VerifyOtpResponse>?> = _verifyOtpResult

    private val _resetPasswordResult = MutableStateFlow<Response<ResetPasswordResponse>?>(null)
    val resetPasswordResult: StateFlow<Response<ResetPasswordResponse>?> = _resetPasswordResult

    fun sendOtp(email: String) {
        viewModelScope.launch {
            try {
                val request = SendOtpRequest(email = email)
                Log.d("AuthViewModel", "Sending OTP request: $request")
                val response = RetrofitClient.authApiService.sendOtp(request)
                _sendOtpResult.value = response
                if (response.isSuccessful) {
                    Log.d("AuthViewModel", "Send OTP successful: ${response.body()?.data}")
                } else {
                    Log.e("AuthViewModel", "Send OTP failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Send OTP error: ${e.message}")
                _sendOtpResult.value = null
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            try {
                val request = VerifyOtpRequest(email = email, otp = otp)
                Log.d("AuthViewModel", "Verify OTP request: $request")
                val response = RetrofitClient.verifyOtp.verifyOtp(request)
                _verifyOtpResult.value = response
                if (response.isSuccessful) {
                    Log.d("AuthViewModel", "Verify OTP successful: ${response.body()?.data}")
                } else {
                    Log.e("AuthViewModel", "Verify OTP failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Verify OTP error: ${e.message}")
                _verifyOtpResult.value = null
            }
        }
    }

    fun verifyOtpChangeEmail(request: VerifyOtpChangeEmailRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.verifyOtp.verifyOtpChangeEmail(request)
                _verifyOtpResult.value = response
                if (response.isSuccessful) {
                    Log.d("AuthViewModel", "Verify OTP successful: ${response.body()?.data}")
                } else {
                    Log.e("AuthViewModel", "Verify OTP failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Verify OTP error: ${e.message}")
                _verifyOtpResult.value = null
            }
        }
    }

    fun resetPassword(email: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                val request = ResetPasswordRequest(
                    email = email,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
                Log.d("AuthViewModel", "Reset password request: $request")
                val response = RetrofitClient.resetPassword.resetPassword(request)
                _resetPasswordResult.value = response
                if (response.isSuccessful) {
                    Log.d("AuthViewModel", "Reset password successful: ${response.body()?.status_code}")
                } else {
                    Log.e("AuthViewModel", "Reset password failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Reset password error: ${e.message}")
                _resetPasswordResult.value = null
            }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val accessToken: String? = null,
    val customer_id: String? = null,
    val error: String? = null,
    val result: Boolean? = null
)

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success(val response: ChangePasswordResponse) : UiState()
    data class Error(val message: String) : UiState()
}