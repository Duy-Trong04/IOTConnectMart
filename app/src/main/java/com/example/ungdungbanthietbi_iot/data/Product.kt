package com.example.ungdungbanthietbi_iot.data

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageUrl: String,
    var isSelected: Boolean
)