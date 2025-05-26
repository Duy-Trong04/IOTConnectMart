package com.example.ungdungbanthietbi_iot.models

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("idPerson") var idPerson:String?,
    @SerializedName("idRole") var idRole:String,
    @SerializedName("username") var username:String,
    @SerializedName("password") var password:String,
    @SerializedName("report") var report:Int,
    @SerializedName("isNew") var isNew:Int,
    @SerializedName("status") var status:Int
)

data class AddAccount(
    @SerializedName("idPerson") var idPerson:String?,
    @SerializedName("username") var username:String,
    @SerializedName("password") var password:String,
)

data class UpdatePassword(
    @SerializedName("idPerson") var idPerson:String?,
    @SerializedName("password") var password:String,
)

data class LoginRequest(
    val username: String,
    val password: String,
    val type: String
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
    val role_id: String,
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