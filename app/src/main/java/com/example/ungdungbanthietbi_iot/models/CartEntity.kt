package com.example.ungdungbanthietbi_iot.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idCustomer: String,
    val idDevice: Int,
    var stock: Int
)