package com.example.ungdungbanthietbi_iot.data.account

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
