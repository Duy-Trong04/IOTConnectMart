package com.example.ungdungbanthietbi_iot.utils

import android.icu.text.SimpleDateFormat
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