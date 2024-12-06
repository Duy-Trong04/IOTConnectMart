package com.example.ungdungbanthietbi_iot

import androidx.compose.runtime.MutableState

data class ProductState(
    val id: Int,
    val name: String,
    val price: Double,
    var quantity: MutableState<Int>,
    val imageUrl: String,
    var isSelected: MutableState<Boolean>
)
