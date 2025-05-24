package com.example.ungdungbanthietbi_iot.models

data class SlideShow(
    val id: Int,
    val idEmployee:String,
    val textButton:String,
    val link:String,
    val image:String,
    val status: Int,
    val created_at:String,
    val updated_at:String
)
