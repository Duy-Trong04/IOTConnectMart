package com.example.ungdungbanthietbi_iot

sealed class Screen (val route:String){
    object Address_Selection:Screen("address_selection_screen")
    object Add_Address:Screen("add_address_screen")
    object Check_Out:Screen("check_out_screen")
    object Order_Detail:Screen("order_detail_screen")
}