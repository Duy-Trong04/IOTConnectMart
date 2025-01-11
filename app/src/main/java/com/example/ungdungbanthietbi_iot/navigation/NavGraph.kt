package com.example.ungdungbanthietbi_iot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.image_device.ImageViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.data.slideshow.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.screen.address.AddAddressScreen
import com.example.ungdungbanthietbi_iot.screen.address.AddressSelectionScreen
import com.example.ungdungbanthietbi_iot.screen.cart.CartScreen
import com.example.ungdungbanthietbi_iot.screen.check_out.CheckoutScreen
import com.example.ungdungbanthietbi_iot.screen.favorite.FavoritesScreen
import com.example.ungdungbanthietbi_iot.screen.signUp_signIn.ForgotPasswordScreen
import com.example.ungdungbanthietbi_iot.screen.home.HomeScreen
import com.example.ungdungbanthietbi_iot.screen.signUp_signIn.IntroScreen
import com.example.ungdungbanthietbi_iot.screen.signUp_signIn.LoginScreen
import com.example.ungdungbanthietbi_iot.screen.order_detail.OrderDetailsScreen
import com.example.ungdungbanthietbi_iot.screen.product_detail.ProductDetailsScreen
import com.example.ungdungbanthietbi_iot.screen.signUp_signIn.RegisterScreen
import com.example.ungdungbanthietbi_iot.screen.signUp_signIn.ResetPasswordScreen
import com.example.ungdungbanthietbi_iot.screen.search.SearchResultsScreen
import com.example.ungdungbanthietbi_iot.screen.search.SearchScreen
import com.example.ungdungbanthietbi_iot.screen.signUp_signIn.VerifyOTPScreen
import com.example.ungdungbanthietbi_iot.screen.Setting.ChangePassword
import com.example.ungdungbanthietbi_iot.screen.Setting.ContactScreen
import com.example.ungdungbanthietbi_iot.screen.personal.AccountSettingsScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditEmailScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditPhoneScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditProfileScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditUsername
import com.example.ungdungbanthietbi_iot.screen.personal.OrderListScreen
import com.example.ungdungbanthietbi_iot.screen.personal.PersonalScreen
import com.example.ungdungbanthietbi_iot.screen.rating.ProductReviewsScreen
import com.example.ungdungbanthietbi_iot.screen.rating.RatingHistoryScreen

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
fun NavGraph(
    navController:NavHostController,
    deviceViewModel: DeviceViewModel,
    slideShowViewModel: SlideShowViewModel,
    imageViewModel: ImageViewModel,
    reviewViewModel: ReviewViewModel){
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
            HomeScreen(navController, deviceViewModel, slideShowViewModel)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(
            route = Screen.Check_Out.route
        ){
            CheckoutScreen(navController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController)
        }
        composable(
            route = Screen.Add_Address.route
        ){
            AddAddressScreen(navController)
        }
        composable(route = Screen.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController)
        }
        composable(
            route = Screen.Address_Selection.route
        ){
            AddressSelectionScreen(navController)
        }
        composable(route = Screen.VerifyOTPScreen.route) {
            VerifyOTPScreen(navController)
        }
        composable(route = Screen.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController)
        }
        composable(
            route = Screen.ProductDetailsScreen.route + "?id={idDevice}",
            arguments = listOf(navArgument("idDevice"){nullable = true})
        ) {
            var idDevice = it.arguments?.getString("idDevice")
            if(idDevice != null){
                ProductDetailsScreen(
                    navController,
                    idDevice,
                    deviceViewModel,
                    imageViewModel,
                    reviewViewModel
                )
            }
        }
        //Màn hình thanh toán
        composable(route = Screen.Cart_Screen.route) {
            CartScreen(navController)
        }
        //Màn hình tất cả đánh giá, bình luận
        composable(route = Screen.Product_Reviews.route+ "?idDevice={idDevice}",
            arguments = listOf(navArgument("idDevice"){nullable = true})
        ) {
            var idDevice = it.arguments?.getString("idDevice")
            if(idDevice != null){
                ProductReviewsScreen(navController, idDevice, reviewViewModel)
            }
        }
        //Màn hình lịch sử đánh giá, bình luận
        composable(route = Screen.Rating_History.route) {
            RatingHistoryScreen(navController)
        }
        //Màn hình xem chi tiết đơn hàng
        composable(
            route = Screen.Order_Detail.route
        ){
            OrderDetailsScreen(navController)
        }
        //Màn hinh tìm kiếm
        composable(
            route = Screen.Search_Screen.route
        ){
            SearchScreen(navController, deviceViewModel)
        }
        //Màn hình kết quả tìm kiếm
        composable(
            route = Screen.Search_Results.route
        ){
            SearchResultsScreen(navController)
        }
        //Màn hình sản phẩm yêu thích
        composable(
            route = Screen.Favorites_Screen.route
        ){
            FavoritesScreen(navController)
        }
        //Đến màn Chỉnh sửa thông tin cá nhân
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(navController)
        }
        composable(Screen.OrderListScreen.route) {
            OrderListScreen(onBack = { navController.popBackStack() })
        }
        //Đường dẫn đến màn hình lịch sử sản phẩm và chỉ đến tab cần đến
        //Ví dụ: ấn vào chờ xác nhận thì đến tab chờ xác nhận
        composable(Screen.OrderListScreen.route + "/{initialPage}") { backStackEntry ->
            val initialPage = backStackEntry.arguments?.getString("initialPage")?.toInt() ?: 0
            OrderListScreen(onBack = { navController.popBackStack() }, initialPage)
        }
        //Cac man hinh thay doi thong tin ca nhan
        //Dẫn đến màn chọn(chỉnh sửa) Username
        composable(Screen.EditUsernamScreen.route) {
            EditUsername(onBack = { navController.popBackStack() })
        }
        //Dẫn đến màn chọn(chỉnh sửa) SỐ ĐIỆN THOẠI
        composable(Screen.EditPhoneScreen.route) {
            EditPhoneScreen(onBack = { navController.popBackStack() })
        }
        //Dẫn đến màn chọn(chỉnh sửa) Email cá nhân
        composable(Screen.EditEmailScreen.route) {
            EditEmailScreen(onBack = { navController.popBackStack() })
        }
        //Dẫn đến Man hinh Setting
        composable(Screen.SettingScreen.route) {
            AccountSettingsScreen(navController,onBack = { navController.popBackStack() })
        }
        //Dẫn đến màn hình liên hệ
        composable(Screen.ContactScreen.route) {
            ContactScreen(onBack = { navController.popBackStack() })
        }
        //Dẫn đến màn hình chỉnh sửa Mật khẩu
        composable(Screen.ChangePassword.route) {
            ChangePassword(onBack = { navController.popBackStack() })
        }
        composable(
            Screen.PersonalScreen.route
        ) {
            PersonalScreen(navController)
        }
    }
}