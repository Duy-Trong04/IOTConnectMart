package com.example.ungdungbanthietbi_iot.navigation

/** Các thành phần đối tượng
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: chưa có
 *
 * Output: Chứa các thành phần đối tượng màn hình
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */

sealed class Screen(var route: String) {
    object LoginScreen : Screen("LoginScreen")
    object RegisterScreen : Screen("RegisterScreen")
    object ForgotPasswordScreen : Screen("ForgotPasswordScreen")
    object VerifyOTPScreen : Screen("VerifyOTPScreen")
    object ResetPasswordScreen : Screen("ResetPasswordScreen")
    object HomeScreen : Screen("HomeScreen")
    object ProductDetailsScreen : Screen("ProductDetailsScreen")
    object IntroScreen : Screen("IntroScreen")

    object Address_Selection: Screen("address_selection_screen")
    object Add_Address: Screen("add_address_screen")
    object Check_Out: Screen("check_out_screen")
    object Order_Detail: Screen("order_detail_screen")
    object Search_Screen: Screen("search_screen")
    object Search_Results: Screen("search_results_screen")
    object Favorites_Screen: Screen("favorites_screen")
    object Cart_Screen:Screen("cart_screen")
    object Product_Reviews:Screen("product_reviews")
    object Rating_Screen:Screen("rating_screen")
    object Rating_History:Screen("rating_history")
    object CheckOutSuccess:Screen("checkout_success")

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