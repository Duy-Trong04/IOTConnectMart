package com.example.ungdungbanthietbi_iot.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import androidx.compose.runtime.Composable
import java.util.Locale

@Composable
fun formatDate(inputDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Định dạng từ API
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()) // Định dạng đầu ra
        val date = inputFormat.parse(inputDate)
        date?.let { outputFormat.format(it) } ?: "Ngày không hợp lệ"
    } catch (e: Exception) {
        "Ngày không hợp lệ"
    }
}
@Composable
fun formatDateTimeZone(dateString: String?): String {
    if (dateString.isNullOrBlank()) {
        return "N/A"
    }
    return try {
        // Parse chuỗi ngày giờ từ định dạng ISO 8601
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(dateString)

        // Format sang định dạng dd/MM/yyyy HH:mm:ss
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault() // Sử dụng múi giờ của thiết bị
        date?.let { formatter.format(it) } ?: "N/A"
    } catch (e: Exception) {
        Log.e("FormatDate", "Lỗi định dạng ngày: $dateString, ${e.message}")
        "N/A"
    }
}