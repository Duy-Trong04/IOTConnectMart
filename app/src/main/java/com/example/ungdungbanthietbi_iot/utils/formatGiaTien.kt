package com.example.ungdungbanthietbi_iot.utils

import java.text.DecimalFormat

//Hàm format tiền
fun formatGiaTien(gia: Double): String {
    val formatter = DecimalFormat("#,###,###")
    return "${formatter.format(gia)} VNĐ"
}

fun formatGiaTienInt(gia: Int): String {
    val formatter = DecimalFormat("#,###,###")
    return "${formatter.format(gia)} VNĐ"
}