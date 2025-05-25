package com.example.ungdungbanthietbi_iot.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ungdungbanthietbi_iot.api.CartDao
import com.example.ungdungbanthietbi_iot.models.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}