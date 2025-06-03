package com.example.ungdungbanthietbi_iot.models

import com.google.gson.annotations.SerializedName

data class SlideShow(
    val id: Int,
    @SerializedName("text_button") val textButton:String,
    val link:String,
    val image:String,
    val status: Int,
    val created_at:String,
    val updated_at:String?,
    val deleted_at:String?
)
