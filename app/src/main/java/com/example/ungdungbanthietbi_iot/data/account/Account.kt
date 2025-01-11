package com.example.ungdungbanthietbi_iot.data.account

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("idPerson")val idPerson: String,
    @SerializedName("idRole")val idRole: String,
    @SerializedName("username")val username: String,
    @SerializedName("password")val password: String,
    @SerializedName("report")val report: String,
    @SerializedName("isNew")val isNew: String,
    @SerializedName("status")val status: String
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