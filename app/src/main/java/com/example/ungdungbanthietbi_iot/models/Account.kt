package com.example.ungdungbanthietbi_iot.models

import com.google.gson.annotations.SerializedName

data class AddAccount(
    @SerializedName("account_id") val accountId: String?,
    @SerializedName("customer_id") val customerId: String?,
    @SerializedName("employee_id") val employeeId: String?,
    @SerializedName("role_id") val roleId: String?,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("verification_code") val verificationCode: String?,
    @SerializedName("verification_expiry") val verificationExpiry: String?,
    @SerializedName("report") val report: Int,
    @SerializedName("is_new") val isNew: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("is_locked") val isLocked: Boolean,
    @SerializedName("locked_at") val lockedAt: String?
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val status_code: Int,
    val data: LoginData
)

data class LoginData(
    val accessToken: String,
    val data: AccountData
)

data class AccountData(
    val account_id: String,
    val customer_id: String?,
    val employee_id: String?,
    val role_id: Int?,
    val username: String,
    val password: String,
    val verification_code: String?,
    val verification_expiry: String?,
    val report: String?,
    val is_new: Boolean?,
    val status: String?,
    val created_at: String,
    val updated_at: String?,
    val deleted_at: String?,
    val is_locked: Boolean,
    val locked_at: String?
)