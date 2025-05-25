package com.example.ungdungbanthietbi_iot.models

data class OrderDetail(
    var id:Int,
    var idOrder:Int,
    var idDevice:Int,
    var price:Double,
    var stock:Int,
    var amount:Double,
    var status:Int
)
