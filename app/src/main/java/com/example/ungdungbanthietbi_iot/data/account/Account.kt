package com.example.ungdungbanthietbi_iot.data.account

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("idPerson")val idPerson: String,
    @SerializedName("username")val username: String,
    @SerializedName("password")val password: String,
    @SerializedName("report")val report: Int,
    @SerializedName("idRole")val idRole: String,
    @SerializedName("status")val status: Int,
    @SerializedName("isNew")val isNew: Boolean
)

//data class LoginRequest(
//    val username: String,
//    val password: String,
//    val type: String
//)

data class LoginResponse(
    val account: Account?,
    val message: String,
)