package com.example.ungdungbanthietbi_iot.models

data class Image(
    val id:Int,
    val idDevice:Int,
    val image:String,
    val created_at:String,
    val updated_at: String,
    val deleted_at: String
)
