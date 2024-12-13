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
}