package com.example.ungdungbanthietbi_iot.models

data class Address(
    var id:Int,
    var idCustomer:String,
    var district:String,
    var city:String,
    var ward:String,
    var street:String,
    var isDefault:Int
)
