package com.example.ungdungbanthietbi_iot.models

data class Notice(
    var id: Int,
    var idUser: String,
    var idRole: String,
    var text:String,
    var type:String,
    var created_at: String,
    var status: Int
)
