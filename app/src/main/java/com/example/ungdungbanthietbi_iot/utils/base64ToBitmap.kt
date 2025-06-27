package com.example.ungdungbanthietbi_iot.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

fun base64ToBitmap(base64String: String?): Bitmap? {
    if (base64String.isNullOrBlank()) return null
    return try {
        // Loại bỏ tiền tố "data:image/jpeg;base64," nếu có
        val cleanBase64 = base64String.replace(Regex("^data:image/[a-zA-Z]+;base64,"), "")
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("Base64ToBitmap", "Lỗi giải mã Base64: ${e.message}")
        null
    }
}