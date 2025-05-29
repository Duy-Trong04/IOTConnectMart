package com.example.ungdungbanthietbi_iot.models

data class Address(
    var id:Int,
    var idCustomer:String,
    var district:String,
    var city:String,
    var ward:String,
    var street:String,
    var isDefault:Int
)

data class AddressBook(
    val id: Int,
    val receiver_name: String,
    val phone: String,
    val district: String,
    val city: String,
    val ward: String,
    val street: String,
    val detail: String,
    val is_default: Int,
    val created_at: String,
    val updated_at: String?,
    val deleted_at: String?
){
    fun getFormattedAddress(): String {
        return "$street, $ward, $district, $city, Việt Nam"
    }
}
