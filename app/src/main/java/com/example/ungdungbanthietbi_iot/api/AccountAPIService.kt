package com.example.ungdungbanthietbi_iot.api

import com.example.ungdungbanthietbi_iot.models.AddAccount
import com.example.ungdungbanthietbi_iot.models.LoginRequest
import com.example.ungdungbanthietbi_iot.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

// ChangePasswordRequest.kt
data class ChangePasswordRequest(
    val username: String,
    val password: String,
    val newPassword: String,
    val confirmPassword: String
)

// ChangePasswordResponse.kt
data class ChangePasswordResponse(
    val status_code: Int
)

// ChangePasswordUiState.kt
data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val statusCode: Int? = null,
    val error: String? = null,
    val result: Boolean? = null
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val confirm_password: String,
    val surname: String,
    val lastname: String,
    val phone: String,
    val email: String,
    val gender: Boolean
)
data class RegisterResponse(
    val status_code: Int,
    val data: AddAccount
)

// Yêu cầu gửi email để nhận OTP
data class SendOtpRequest(
    val email: String
)

// Phản hồi từ API gửi OTP
data class SendOtpResponse(
    val status_code: Int,
    val data: OtpData
)

data class OtpData(
    val message: String,
    val otp: String
)

// Yêu cầu xác minh OTP
data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

// Yêu cầu xác minh OTP
data class VerifyOtpChangeEmailRequest(
    val account_id: String,
    val email: String,
    val otp: String
)

// Phản hồi từ API xác minh OTP
data class VerifyOtpResponse(
    val status_code: Int,
    val data: VerifyData
)

data class VerifyData(
    val message: String
)

// Yêu cầu đặt lại mật khẩu
data class ResetPasswordRequest(
    val email: String,
    val newPassword: String,
    val confirmPassword: String
)

// Phản hồi từ API đặt lại mật khẩu
data class ResetPasswordResponse(
    val status_code: Int
)

interface AccuntAPIService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun addAccount(
        @Body account: RegisterRequest
    ): Response<RegisterResponse>

    @PATCH("auth/account/changed-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    @POST("auth/send-otp")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<SendOtpResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("auth/verify-otp-change-email")
    suspend fun verifyOtpChangeEmail(
        @Body request: VerifyOtpChangeEmailRequest
    ): Response<VerifyOtpResponse>

    @POST("auth/account/change-password-forgot")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResetPasswordResponse>
}