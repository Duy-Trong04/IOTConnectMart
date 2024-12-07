package com.example.ungdungbanthietbi_iot.naviation

sealed class Screen(val route: String) {
    //Các màn hình Personal (Hồ sơ cá nhân)
    object PersonalScreen : Screen("PersonalScreen")
    object EditProfileScreen : Screen("EditProfileScreen")
    object OrderListScreen: Screen("OrderListScreen")

    //Các màn hình chỉnh sửa thông tin người dùng
    object EditUsernamScreen :Screen("EditUsernamScreen")
    object GenderSelectionScreen :Screen("GenderSelectionScreen")
    object DatePickerScreen :Screen("CalendarScreen")
    object EditPhoneScreen :Screen("EditPhoneScreen")
    object EditEmailScreen :Screen("EditEmailScreen")

    //Màn hình Setting
    object SettingScreen :Screen("SettingScreen")
    object ContactScreen :Screen("ContactScreen")
    object ChangePassword :Screen("ChangePassword")
}