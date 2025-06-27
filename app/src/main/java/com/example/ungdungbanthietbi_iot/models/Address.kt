package com.example.ungdungbanthietbi_iot.models

data class AddressBook(
    val id: Int,
    val customer_id :String,
    val receiver_name: String,
    val phone: String,
    val district: String,
    val city: String,
    val ward: String,
    val street: String,
    val detail: String,
    val is_default: Boolean,
    val created_at: String,
    val updated_at: String?,
    val deleted_at: String?
){
    fun getFormattedAddress(): String {
        return "$detail $street, $ward, $district, $city, Việt Nam"
    }
}


data class AddressResponsePublic<T>(
    val code: Int,
    val message: String,
    val data: T
)

data class Province(
    val ProvinceID: Int,
    val ProvinceName: String,
    val Code: Int,
    val NameExtension: List<String>,
    val IsEnable: Int,
    val RegionID: Int,
    val CanUpdateCOD: Boolean,
    val Status: Int,
    val CreatedAt: String,
    val UpdatedAt: String
)

data class District(
    val DistrictID: Int,
    val ProvinceID: Int,
    val DistrictName: String,
    val Code: Int,
    val Type: Int,
    val SupportType: Int,
    val NameExtension: List<String>,
    val IsEnable: Int,
    val CanUpdateCOD: Boolean,
    val Status: Int,
    val CreatedDate: String,
    val UpdatedDate: String
)

data class Ward(
    val WardCode: Int,
    val DistrictID: Int,
    val WardName: String,
    val NameExtension: List<String>,
    val CanUpdateCOD: Boolean,
    val SupportType: Int,
    val Status: Int,
    val CreatedDate: String,
    val UpdatedDate: String
)