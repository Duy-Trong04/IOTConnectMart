package com.example.ungdungbanthietbi_iot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
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
import com.example.ungdungbanthietbi_iot.screen.address.UpdateAddress
import com.example.ungdungbanthietbi_iot.screen.check_out.CheckOutSuccessScreen
import com.example.ungdungbanthietbi_iot.screen.personal.AccountSettingsScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditEmailScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditPhoneScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditProfileScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditUsername
import com.example.ungdungbanthietbi_iot.screen.personal.OrderListScreen
import com.example.ungdungbanthietbi_iot.screen.personal.PersonalScreen
import com.example.ungdungbanthietbi_iot.screen.rating.ProductReviewsScreen
import com.example.ungdungbanthietbi_iot.screen.rating.RatingHistoryScreen
import com.example.ungdungbanthietbi_iot.ui.theme.parseSelectedProducts

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
    reviewViewModel: ReviewViewModel,
    accountViewModel: AccountViewModel){
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

        //Home chưa đăng nhập
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController, deviceViewModel, slideShowViewModel, null)
        }

        //Home đã có tài khoản đăng nhập
        composable(route = Screen.HomeScreen.route + "?username={username}",
            arguments = listOf(
                navArgument("username"){type = NavType.StringType }
            )
        ){
            val username = it.arguments?.getString("username")
            HomeScreen(navController, deviceViewModel, slideShowViewModel, username)
        }

        //Màn hình đăng nhập
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController, accountViewModel)
        }

        //Màn hình thanh toán
        composable(
            route = Screen.Check_Out.route + "?selectedProducts={selectedProducts}&tongtien={tongtien}&username={username}",
            arguments = listOf(
                navArgument("selectedProducts") {type = NavType.StringType },
                navArgument("tongtien") { type = NavType.StringType},
                navArgument("username") {type = NavType.StringType }
            )
        ){ backStackEntry ->
            // Lấy chuỗi selectedProducts từ tham số điều hướng
            val selectedProductsString = backStackEntry.arguments?.getString("selectedProducts")

            // Gọi hàm parseSelectedProducts để chuyển chuỗi thành danh sách Triple<Int, Int, Int>
            val selectedProducts = selectedProductsString?.let { parseSelectedProducts(it) } ?: emptyList()

            // Chuyển đổi tongtien từ String sang Int, nếu không có giá trị thì mặc định là 0
            val tongtien = backStackEntry.arguments?.getString("tongtien")?.toDoubleOrNull() ?: 0.0
            val username = backStackEntry.arguments?.getString("username") ?: ""

            CheckoutScreen(navController, selectedProducts, tongtien = tongtien, username = username)
        }
        //Màn hình đăng ký
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController)
        }
        //Màn hình thêm địa chỉ
        composable(
            route = Screen.Add_Address.route + "?idCustomer={idCustomer}",
            arguments = listOf(
                navArgument("idCustomer") {type = NavType.StringType }
            )
        ){
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            AddAddressScreen(navController, idCustomer)
        }

        //Màn hình update địa chỉ
        composable(
            route = Screen.Update_Address.route + "?idCustomer={idCustomer}&id={id}",
            arguments = listOf(
                navArgument("idCustomer") {type = NavType.StringType },
                navArgument("id") {type = NavType.IntType }
            )
        ){
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            val id = it.arguments?.getInt("id") ?: 0
            UpdateAddress(navController, idCustomer, id)
        }

        //Màn hình Reset Pass
        composable(route = Screen.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController)
        }
        //Màn hình chọn địa chỉ
        composable(
            route = Screen.Address_Selection.route + "?idCustomer={idCustomer}",
            arguments = listOf(
                navArgument("idCustomer") {type = NavType.StringType }
            )
        ){
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            AddressSelectionScreen(navController, idCustomer)
        }
        //Màn hình Xác nhận OTP
        composable(route = Screen.VerifyOTPScreen.route) {
            VerifyOTPScreen(navController)
        }
        //Màn hình quên mật khẩu
        composable(route = Screen.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController)
        }
        //Màn hình chi tiêt sản phẩm
        composable(
            route = Screen.ProductDetailsScreen.route + "?id={idDevice}&idCustomer={idCustomer}&username={username}",
            arguments = listOf(
                navArgument("idDevice"){nullable = true},
                navArgument("idCustomer"){nullable = true},
                navArgument("username"){nullable = true}
            )
        ) {
            val idDevice = it.arguments?.getString("idDevice")
            val idCustomer = it.arguments?.getString("idCustomer")
            val username = it.arguments?.getString("username")
            if(idDevice != null){
                ProductDetailsScreen(
                    navController,
                    idDevice,
                    idCustomer,
                    username,
                    deviceViewModel,
                    imageViewModel,
                    reviewViewModel
                )
            }
        }
        //Màn hình giỏ hàng
        composable(route = Screen.Cart_Screen.route + "?idCustomer={idCustomer}&username={username}",
            arguments = listOf(
                navArgument("idCustomer"){type = NavType.StringType },
                navArgument("username") {type = NavType.StringType }
            )
        ) {
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            val username = it.arguments?.getString("username") ?: ""
            CartScreen(navController, idCustomer, username)
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
        composable(
            route = Screen.CheckOutSuccess.route  + "?username={username}",
            arguments = listOf(
                navArgument("username") {type = NavType.StringType}
            )
        ){
            val username = it.arguments?.getString("username") ?: ""
            CheckOutSuccessScreen(navController, username)
        }
        //Màn hình lịch sử đánh giá, bình luận
        composable(route = Screen.Rating_History.route) {
            RatingHistoryScreen(navController)
        }
        //Màn hình xem chi tiết đơn hàng
        composable(
            route = Screen.Order_Detail.route + "?id={id}&totalAmount={totalAmount}",
            arguments = listOf(
                navArgument("id") {type = NavType.IntType},
                navArgument("totalAmount") {type = NavType.StringType}
            )
        ){
            val id = it.arguments?.getInt("id") ?: 0
            val totalAmount = it.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
            OrderDetailsScreen(navController, id, totalAmount)
        }
        //Màn hinh tìm kiếm đã đăng nhập
        composable(
            route = Screen.Search_Screen.route + "?username={username}",
            arguments = listOf(
                navArgument("username") {type = NavType.StringType }
            )
        ){
            val username = it.arguments?.getString("username") ?: ""
            SearchScreen(navController, deviceViewModel, username)
        }

        //Màn hình tìm kiếm chưa có tài khoản
        composable(
            route = Screen.Search_Screen.route
        ){
            SearchScreen(navController, deviceViewModel, null)
        }

        //Màn hình kết quả tìm kiếm
        composable(
            route = Screen.Search_Results.route
        ){
            SearchResultsScreen(navController)
        }

        //Màn hình sản phẩm yêu thích
        composable(
            route = Screen.Favorites_Screen.route + "?idCustomer={idCustomer}&username={username}",
            arguments = listOf(
                navArgument("idCustomer"){type = NavType.StringType },
                navArgument("username") {type = NavType.StringType }
            )
        ){
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            val username = it.arguments?.getString("username") ?: ""
            FavoritesScreen(navController, idCustomer, username)
        }

        //Đến màn Chỉnh sửa thông tin cá nhân
        composable(Screen.EditProfileScreen.route + "?username={username}",
            arguments = listOf(
                navArgument("username") {type = NavType.StringType}
            )
        ) {
            val username = it.arguments?.getString("username") ?: ""
            EditProfileScreen(navController, username)
        }

        //Lịch sử mua hàng (quản lý đơn hàng)
        composable(Screen.OrderListScreen.route +"?idCustomer={idCustomer}",
            arguments = listOf(
                navArgument("idCustomer") {type = NavType.StringType}
            )
        ) {
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            OrderListScreen(navController, idCustomer)
        }
//        //Đường dẫn đến màn hình lịch sử sản phẩm và chỉ đến tab cần đến
//        //Ví dụ: ấn vào chờ xác nhận thì đến tab chờ xác nhận
//        composable(Screen.OrderListScreen.route + "/{initialPage}" + "?idCustomer={idCustomer}") { backStackEntry ->
//            val initialPage = backStackEntry.arguments?.getString("initialPage")?.toInt() ?: 0
//            val idCustomer = backStackEntry.arguments?.getString("idCustomer") ?: ""
//            OrderListScreen(onBack = { navController.popBackStack() }, initialPage, idCustomer)
//        }


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
            Screen.PersonalScreen.route + "?username={username}",
            arguments = listOf(
                navArgument("username") {type = NavType.StringType }
            )
        ) {
            val username = it.arguments?.getString("username") ?: ""
            PersonalScreen(navController, username, deviceViewModel)
        }
    }
}