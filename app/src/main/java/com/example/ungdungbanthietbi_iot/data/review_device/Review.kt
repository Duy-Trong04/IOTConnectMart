package com.example.ungdungbanthietbi_iot.data.review_device

data class Review(
    val idReview:Int,
    val idCustomer:String,
    val idEmployee:String,
    val idDevice:Int,
    val comment:String,
    val rating:Int,
    val response:String,
    val note:String,
    val created_at:String,
    val updated_at:String,
    val status:Int
)
