package com.example.ungdungbanthietbi_iot.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customer_id : String,
    val product_id : Int,
    var quantity: Int,
    val selected: Boolean,
    val created_at: String,
    val updated_at: String?,
    val deleted_at: String?
)