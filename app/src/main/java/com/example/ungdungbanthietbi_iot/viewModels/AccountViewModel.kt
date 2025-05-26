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
import com.example.ungdungbanthietbi_iot.config.RetrofitClient
import com.example.ungdungbanthietbi_iot.api.CheckLoginResponse
import com.example.ungdungbanthietbi_iot.dataStore
import com.example.ungdungbanthietbi_iot.models.Account
import com.example.ungdungbanthietbi_iot.models.AddAccount
import com.example.ungdungbanthietbi_iot.models.LoginRequest
import com.example.ungdungbanthietbi_iot.models.LoginResponse
import com.example.ungdungbanthietbi_iot.models.UpdatePassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountViewModel:ViewModel() {
    var account: Account? by mutableStateOf(null)
        private set

    var accountById: Account? by mutableStateOf(null)
        private set

    private var accountAddResult by mutableStateOf("")

    private val _loginResult = MutableStateFlow<CheckLoginResponse?>(null)
    val loginResult: StateFlow<CheckLoginResponse?> = _loginResult



    private var accountUpdateResult by mutableStateOf("")

    var username: String? = null
    var idPerson: String? = null

    private val _accountCheckResult = mutableStateOf<Boolean?>(null)
    val accountCheckResult: State<Boolean?> = _accountCheckResult


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
    suspend fun logout(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
        _loginResult.value = null
    }

    fun getUserByUsername(username: String) {
        this.username = username
        viewModelScope.launch(Dispatchers.IO) {
            try {
                account = RetrofitClient.accountAPIService.getAccountByUsername(username)
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Error getting SanPham", e)
            }
        }
    }
    fun getAccountById(idPerson: String) {
        this.idPerson = idPerson
        viewModelScope.launch(Dispatchers.IO) {
            try {
                accountById = RetrofitClient.accountAPIService.getAccountById(idPerson)
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Error getting account", e)
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
                    _accountCheckResult.value= response
                Log.d("AccountViewModel", "check_Dka: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                _accountCheckResult.value= false
            }
        }
    }

    fun updatePassword(account: UpdatePassword) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.accountAPIService.updatePassword(account)
                // Giả sử response.success là một Boolean xác nhận xem khách hàng có hợp lệ không
                //_customerCheckResult.value = response
                Log.d("AccountViewModel", "check_Dkc: $response")
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Lỗi kết nối: ${e.message}")
                //_customerCheckResult.value = false
            }
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.accountAPIService.updateAccount(account)
                }
                accountUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                accountUpdateResult = "Lỗi khi cập nhật account: ${e.message}"
                Log.e("Account Error", "Lỗi khi cập nhật account: ${e.message}")
            }
        }
    }
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

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
                        error = null,
                        result = true
                    )
                } else {
                    _loginUiState.value = LoginUiState(
                        isLoading = false,
                        accessToken = null,
                        error = "Đăng nhập thất bại: Mã trạng thái ${response.status_code}",
                        result = false
                    )
                }
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState(
                    isLoading = false,
                    accessToken = null,
                    error = e.message ?: "Đã xảy ra lỗi khi đăng nhập",
                    result = false
                )
            }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val accessToken: String? = null,
    val error: String? = null,
    val result: Boolean? = null
)