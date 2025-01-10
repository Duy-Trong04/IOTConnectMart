package com.example.ungdungbanthietbi_iot.data.device

data class Device(
    val idDevice: Int, // Kiểu Int cho ID thiết bị
    val name: String, // Kiểu String cho tên thiết bị
    val slug: String, // Kiểu String cho đường dẫn (slug)
    val description: String, // Kiểu String cho mô tả HTML
    val descriptionNormal: String, // Kiểu String cho mô tả thông thường
    val image: String, // Kiểu String cho tên ảnh
    val sellingPrice: Double, // Kiểu Double cho giá bán (có phần thập phân)
    val idCategory: Int, // Kiểu Int cho ID danh mục
    val created_at: String, // Kiểu String cho ngày tạo (định dạng ngày giờ)
    val update_at: String, // Kiểu String cho ngày cập nhật (định dạng ngày giờ)
    val isHide: Int, // Kiểu Int (0 hoặc 1) biểu thị trạng thái ẩn/hiện
    val status: Int // Kiểu Int (0 hoặc 1) biểu thị trạng thái hoạt động
)
data class DeviceResponse(
    val results: List<Device>
)

data class CategoryDevice(
    val id: Int,
    val nameCategory: String
)