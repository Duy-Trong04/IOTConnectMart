package com.example.ungdungbanthietbi_iot.data.customer

import com.google.gson.annotations.SerializedName

data class Customer(
    val id:String,
    val surname:String,
    val lastName:String,
    val phone:String,
    val email:String,
    val birthdate:String,
    val gender:Int,
    val created_at:String,
    val update_at:String,
    val status:String
)

data class AddCustomer(
    @SerializedName("id") val id:String,
    @SerializedName("surname") val surname:String,
    @SerializedName("lastName") val lastName:String,
    @SerializedName("phone") val phone:String,
)