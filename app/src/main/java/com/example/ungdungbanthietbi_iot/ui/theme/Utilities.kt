package com.example.ungdungbanthietbi_iot.ui.theme

fun parseSelectedProducts(selectedProductsString: String?): List<Triple<String, Int, Int>> {
    return selectedProductsString?.split(",")
        ?.mapNotNull { productString ->
            val parts = productString.split(":")
            if (parts.size == 3) {
                val maSanPham = parts[0]
                val soLuong = parts[1].toIntOrNull()
                val maGioHang = parts[2].toIntOrNull()
                if (soLuong != null && maGioHang != null) {
                    Triple(maSanPham, soLuong, maGioHang)
                } else null
            } else null
        } ?: emptyList()
}


