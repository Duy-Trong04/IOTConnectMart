package com.example.ungdungbanthietbi_iot

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/** Chuyển hướng (NavGraph)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavHostController
 *
 * Output: Chuyển hướng giữa các màn hình
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        // Màn hình đầu tiên hiển thị
        startDestination = Screen.IntroScreen.route
    ){
        // Màn hình IntroScreen sau khoảng thời gian quy định thì chuyển sang màn hình trang chủ HomeScreen
        composable(route = Screen.IntroScreen.route){
            IntroScreen(onTimeout = {
                navController.navigate("HomeScreen"){
                    // Xóa màn hình IntroScreen khỏi ngăn xếp
                    popUpTo(Screen.IntroScreen.route){
                        inclusive = true
                    }
                }
            })
            }
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController)
        }
        composable(route = Screen.ResetPasswordScreen.route){
            ResetPasswordScreen(navController)
        }
        composable(route = Screen.VerifyOTPScreen.route) {
            VerifyOTPScreen(navController)
        }
        composable(route = Screen.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController)
        }
        composable(route = Screen.ProductDetailsScreen.route) {
            ProductDetailsScreen(navController)
        }
    }
}