package com.example.ungdungbanthietbi_iot.data.cart

import androidx.room.*

@Dao
interface CartDao {
    @Query("SELECT * FROM cart WHERE idCustomer = :idCustomer")
    suspend fun getCartByIdCustomer(idCustomer: String): List<CartEntity>

    @Insert
    suspend fun insertCart(cart: CartEntity)

    @Update
    suspend fun updateCart(cart: CartEntity)

    @Delete
    suspend fun deleteCart(cart: CartEntity)

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun deleteCartById(id: Int)

    @Query("DELETE FROM cart WHERE idCustomer = :idCustomer")
    suspend fun deleteAllCartsByCustomer(idCustomer: String)
}