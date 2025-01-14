package com.example.ungdungbanthietbi_iot.data.address_book

data class Address(
    var id:Int,
    var idCustomer:String,
    var district:String,
    var city:String,
    var ward:String,
    var street:String,
    var isDefault:Int
)
