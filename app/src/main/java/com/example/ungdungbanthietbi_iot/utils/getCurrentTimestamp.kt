package com.example.ungdungbanthietbi_iot.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


// Hàm giả lập lấy thời gian hiện tại, bạn có thể thay thế bằng cách lấy thời gian theo chuẩn của hệ thống
fun getCurrentTimestamp(): String {
    // Ví dụ: trả về thời gian hiện tại theo định dạng "yyyy-MM-dd HH:mm:ss"
    val current = java.util.Calendar.getInstance().time
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    return formatter.format(current)
}

fun getCurrentTimestampEX(): String {
    val current = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC") // Đảm bảo sử dụng UTC
    return formatter.format(current)
}