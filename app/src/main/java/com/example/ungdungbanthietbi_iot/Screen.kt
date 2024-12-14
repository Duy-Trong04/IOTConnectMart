package com.example.ungdungbanthietbi_iot

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


    object Address_Selection:Screen("address_selection_screen")
    object Add_Address:Screen("add_address_screen")
    object Check_Out:Screen("check_out_screen")
    object Order_Detail:Screen("order_detail_screen")
    object Search_Screen:Screen("search_screen")
    object Search_Results:Screen("search_results_screen")
    object Favorites_Screen:Screen("favorites_screen")
}