package com.example.ungdungbanthietbi_iot.utils


// Hàm giả lập lấy thời gian hiện tại, bạn có thể thay thế bằng cách lấy thời gian theo chuẩn của hệ thống
fun getCurrentTimestamp(): String {
    // Ví dụ: trả về thời gian hiện tại theo định dạng "yyyy-MM-dd HH:mm:ss"
    val current = java.util.Calendar.getInstance().time
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    return formatter.format(current)
}