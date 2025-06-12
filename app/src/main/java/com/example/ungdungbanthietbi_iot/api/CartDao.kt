package com.example.ungdungbanthietbi_iot.api

import androidx.room.*
import com.example.ungdungbanthietbi_iot.models.CartEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart WHERE customer_id = :customer_id")
    suspend fun getCartByIdCustomer(customer_id: String): List<CartEntity>

    @Insert
    suspend fun insertCart(cart: CartEntity)

    @Update
    suspend fun updateCart(cart: CartEntity)

    @Delete
    suspend fun deleteCart(cart: CartEntity)

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun deleteCartById(id: Int)

    @Query("DELETE FROM cart WHERE customer_id = :customer_id")
    suspend fun deleteAllCartsByCustomer(customer_id: String)
}