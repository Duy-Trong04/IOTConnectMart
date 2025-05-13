package com.example.ungdungbanthietbi_iot.data.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idCustomer: String,
    val idDevice: Int,
    var stock: Int
)