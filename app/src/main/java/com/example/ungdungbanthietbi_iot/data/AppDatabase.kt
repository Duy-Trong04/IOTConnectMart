package com.example.ungdungbanthietbi_iot.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ungdungbanthietbi_iot.data.cart.CartDao
import com.example.ungdungbanthietbi_iot.data.cart.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}