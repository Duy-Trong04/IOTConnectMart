package com.example.ungdungbanthietbi_iot

data class ProductState(
    val id: Int,
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageUrl: String,
    var isSelected: Boolean
)
