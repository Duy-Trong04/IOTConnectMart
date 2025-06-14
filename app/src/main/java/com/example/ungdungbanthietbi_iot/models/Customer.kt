package com.example.ungdungbanthietbi_iot.models

data class Customer(
    val id:String,
    val surname:String,
    val lastname:String,
    val image: String?,
    val phone:String,
    val email:String,
    val email_verified: Boolean,
    val birthdate:String?,
    val gender:Boolean,
    val created_at:String,
    val update_at:String?,
    val delete_at: String?,
    val account: List<AccountDataResponse>,
    val fullname: String
)

data class AccountDataResponse(
    val account_id: String,
    val username: String,
    val status: String
)