package com.example.ungdungbanthietbi_iot.views.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream

// hình ảnh base64
fun compressImage(inputImage: ByteArray, quality: Int, maxFileSizeKB: Int): ByteArray? {
    try {
        var bitmap = BitmapFactory.decodeByteArray(inputImage, 0, inputImage.size)
        val outputStream = ByteArrayOutputStream()
        var currentQuality = quality

        do {
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)

            if (outputStream.size() / 1024 > maxFileSizeKB) {
                bitmap = resizeBitmap(bitmap, bitmap.width / 2, bitmap.height / 2)
            }
            currentQuality -= 10
        } while (outputStream.size() / 1024 > maxFileSizeKB && currentQuality > 10)

        return outputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}

fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { it.readBytes() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}

fun isValidBase64(base64: String?): Boolean {
    return try {
        Base64.decode(base64?.replace("data:image/jpeg;base64,", ""), Base64.DEFAULT)
        true
    } catch (e: IllegalArgumentException) {
        Log.e("ImagePicker", "Invalid Base64 string: ${base64?.take(100)}")
        false
    }
}

fun cleanBase64(base64: String?): String? {
    return base64?.replace("\n", "")?.replace("\r", "")?.trim()
}

fun base64ToBitmap(base64: String?): Bitmap? {
    return try {
        val cleanBase64 = cleanBase64(base64) ?: return null
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("ImagePicker", "Error decoding Base64: ${e.message}")
        null
    }
}