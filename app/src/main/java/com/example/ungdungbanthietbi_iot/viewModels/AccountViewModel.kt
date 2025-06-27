package com.example.ungdungbanthietbi_iot.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.example.ungdungbanthietbi_iot.api.ChangePasswordRequest
import com.example.ungdungbanthietbi_iot.api.ChangePasswordResponse
import com.example.ungdungbanthietbi_iot.api.ChangePasswordUiState
import com.example.ungdungbanthietbi_iot.api.LoginIOTRequest
import com.example.ungdungbanthietbi_iot.api.LoginIOTResponse
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
import com.example.ungdungbanthietbi_iot.models.AddAccount
import com.example.ungdungbanthietbi_iot.models.LoginRequest
import com.example.ungdungbanthietbi_iot.models.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.provider.Settings
import retrofit2.HttpException
import retrofit2.Response
import java.util.UUID

class AccountViewModel:ViewModel() {
    var username: String? = null

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun logout(context: Context) {
        viewModelScope.launch {
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
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _registerUiState.value = RegisterUiState.Loading
            try {
                val response = RetrofitClient.accountAPIService.addAccount(request)
                if (response.isSuccessful && response.body()?.status_code == 200 && response.body()?.data != null) {
                    _registerUiState.value = RegisterUiState.Success(response.body()!!.data!!)
                    Log.d("AccountViewModel", "Đăng ký thành công: ${response.body()?.data}")
                } else {
                    // Xử lý các phản hồi không thành công (200 hoặc 400)
                    val errorBody = if (response.isSuccessful) response.body() else {
                        // Phân tích body của phản hồi lỗi (như 400)
                        val errorJson = response.errorBody()?.string()
                        try {
                            Gson().fromJson(errorJson, RegisterResponse::class.java)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    val error = errorBody?.errors?.firstOrNull()
                    val errorMessage = when (error?.code) {
                        1409 -> "Email đã tồn tại.\nVui lòng sử dụng email khác."
                        1621 -> "Tài khoản đã tồn tại\nVui lòng chọn tên tài khoản khác."
                        else -> error?.message ?: "Đăng ký thất bại: ${response.message()}"
                    }
                    _registerUiState.value = RegisterUiState.Error(errorMessage)
                    Log.e("AccountViewModel", "Đăng ký thất bại: $errorMessage, ErrorBody: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _registerUiState.value = RegisterUiState.Error("Lỗi mạng: ${e.message}")
                Log.e("RegisterViewModel", "Lỗi khi đăng ký: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun changePassword(token: String, request: ChangePasswordRequest) {
        viewModelScope.launch {
            _uiState.value = ChangePasswordUiState(isLoading = true)
            try {
                val tokenUser = "Bearer $token"
                val response: Response<ChangePasswordResponse> = RetrofitClient.accountAPIService.changePassword(tokenUser, request)
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
                    password = password
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

    @SuppressLint("HardwareIds")
    fun login(context: Context, username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)
            Log.d("Login", "Bắt đầu đăng nhập - username: $username, password: $password")

            try {
                val deviceName = Build.MODEL
                Log.d("Login", "deviceName: $deviceName")
                val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: UUID.randomUUID().toString()
                Log.d("Login", "deviceId: $deviceId")
                val deviceUuid = UUID.randomUUID().toString()
                Log.d("Login", "deviceUuid: $deviceUuid")

                val request = LoginIOTRequest(
                    username = username,
                    password = password,
                    rememberMe = true,
                    deviceName = deviceName,
                    deviceId = deviceId,
                    deviceUuid = ""
                )
                Log.d("Login", "Request: $request")

                val response: LoginIOTResponse = RetrofitClient.accountAPIServiceIOT.loginIOT(request)
                Log.d("Login", "Response: $response")

                _loginUiState.value = LoginUiState(
                    isLoading = false,
                    accessToken = response.accessToken,
                    customer_id = response.customer_id,
                    error = null,
                    result = true
                )
                Log.d("Login", "Thành công - accessToken: ${response.accessToken}")
            } catch (e: Exception) {
                Log.e("Login", "Lỗi: ${e.message}", e)
                if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("Login", "Nội dung lỗi: $errorBody")
                }
                _loginUiState.value = LoginUiState(
                    isLoading = false,
                    accessToken = null,
                    customer_id = null,
                    error = e.message ?: "Đã xảy ra lỗi khi đăng nhập",
                    result = false
                )
                Log.d("Login", "Thất bại - error: ${e.message}")
            }
        }
    }


    private val _sendOtpResult = MutableStateFlow<Response<SendOtpResponse>?>(null)
    val sendOtpResult: StateFlow<Response<SendOtpResponse>?> = _sendOtpResult

    private val _verifyOtpResult = MutableStateFlow<VerifyOtpUiState>(VerifyOtpUiState.Idle)
    val verifyOtpResult: StateFlow<VerifyOtpUiState> = _verifyOtpResult.asStateFlow()

    private val _verifyEmailResult = MutableStateFlow<VerifyEmailUiState>(VerifyEmailUiState.Idle)
    val verifyEmailResult: StateFlow<VerifyEmailUiState> = _verifyEmailResult.asStateFlow()

    private val _resetPasswordResult = MutableStateFlow<Response<ResetPasswordResponse>?>(null)
    val resetPasswordResult: StateFlow<Response<ResetPasswordResponse>?> = _resetPasswordResult


    fun sendOtp(email: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Bắt đầu loading
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
            }finally {
                _isLoading.value = false // Kết thúc loading
            }
        }
    }
    fun resetSendOtpResult() {
        _sendOtpResult.value = null
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _verifyOtpResult.value = VerifyOtpUiState.Loading
            try {
                val request = VerifyOtpRequest(email = email, otp = otp)
                Log.d("AuthViewModel", "Verify OTP request: $request")
                val response = RetrofitClient.accountAPIService.verifyOtp(request)
                if (response.isSuccessful && response.body()?.status_code == 200 && response.body()?.data != null) {
                    _verifyOtpResult.value = VerifyOtpUiState.Success(response.body()!!.data!!.message)
                    Log.d("AuthViewModel", "Verify OTP successful: ${response.body()?.data}")
                } else {
                    val errorBody = if (response.isSuccessful) response.body() else {
                        val errorJson = response.errorBody()?.string()
                        try {
                            Gson().fromJson(errorJson, VerifyOtpResponse::class.java)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    val error = errorBody?.errors?.firstOrNull()
                    val errorMessage = when (error?.code) {
                        1616 -> "Mã xác thực không khớp. Vui lòng thử lại."
                        else -> error?.message ?: "Vui lòng nhập đầy đủ 6 chữ số !"
                    }
                    _verifyOtpResult.value = VerifyOtpUiState.Error(errorMessage)
                    Log.e("AuthViewModel", "Verify OTP failed: $errorMessage, ErrorBody: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                val errorMessage = "Lỗi mạng: ${e.message}"
                _verifyOtpResult.value = VerifyOtpUiState.Error(errorMessage)
                Log.e("AuthViewModel", "Verify OTP error: $errorMessage")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun verifyOtpChangeEmail(request: VerifyOtpChangeEmailRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _verifyEmailResult.value = VerifyEmailUiState.Loading
            try {
                val response = RetrofitClient.verifyOtp.verifyOtpChangeEmail(request)
                if (response.isSuccessful && response.body()?.status_code == 200 && response.body()?.data != null) {
                    _verifyEmailResult.value = VerifyEmailUiState.Success(response.body()!!.data!!.message)
                    Log.d("AuthViewModel", "Verify Email successful: ${response.body()?.data}")
                } else {
                    val errorBody = if (response.isSuccessful) response.body() else {
                        val errorJson = response.errorBody()?.string()
                        try {
                            Gson().fromJson(errorJson, VerifyOtpResponse::class.java)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    val error = errorBody?.errors?.firstOrNull()
                    val errorMessage = when (error?.code) {
                        1616 -> "Mã xác thực không khớp. Vui lòng thử lại !"
                        else -> error?.message ?: "Vui lòng nhập đầy đủ 6 chữ số !"
                    }
                    _verifyEmailResult.value = VerifyEmailUiState.Error(errorMessage)
                    Log.e("AuthViewModel", "Verify OTP failed: $errorMessage, ErrorBody: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                val errorMessage = "Lỗi mạng: ${e.message}"
                _verifyEmailResult.value = VerifyEmailUiState.Error(errorMessage)
                Log.e("AuthViewModel", "Verify OTP error: $errorMessage")
            } finally {
                _isLoading.value = false
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

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val account: AddAccount) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success(val response: ChangePasswordResponse) : UiState()
    data class Error(val message: String) : UiState()
}

sealed class VerifyOtpUiState {
    object Idle : VerifyOtpUiState()
    object Loading : VerifyOtpUiState()
    data class Success(val message: String) : VerifyOtpUiState()
    data class Error(val message: String) : VerifyOtpUiState()
}

sealed class VerifyEmailUiState {
    object Idle : VerifyEmailUiState()
    object Loading : VerifyEmailUiState()
    data class Success(val message: String) : VerifyEmailUiState()
    data class Error(val message: String) : VerifyEmailUiState()
}