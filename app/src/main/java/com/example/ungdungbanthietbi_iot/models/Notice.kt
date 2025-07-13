package com.example.ungdungbanthietbi_iot.models

data class Notice(
    val id: Int,
    val account_id: String,
    val role_id: Int?,
    val text:String?,
    val type:String?,
    val is_read: Boolean?,
    val id_reference: String?,
    val created_at: String?,
    val deleted_at: String?
)
