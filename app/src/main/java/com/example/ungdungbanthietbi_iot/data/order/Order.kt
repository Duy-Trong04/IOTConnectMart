package com.example.ungdungbanthietbi_iot.data.order

data class Order(
    val id:Int,
    val idCustomer:String,
    val totalAmount:Double,
    val paymentMethod:String,
    val address:String,
    val accountNumber:String,
    val phone:String,
    val nameRecipient:String,
    val note:String,
    val platformOrder:String,
    val created_at:String,
    val updated_at:String,
    val accept_at:String,
    val idEmployee:String,
    val status:Int
)
