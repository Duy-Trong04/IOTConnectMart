package com.example.ungdungbanthietbi_iot.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ImageViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.viewModels.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.views.address.AddAddressScreen
import com.example.ungdungbanthietbi_iot.views.address.AddressSelectionScreen
import com.example.ungdungbanthietbi_iot.views.cart.CartScreen
import com.example.ungdungbanthietbi_iot.views.check_out.CheckoutScreen
import com.example.ungdungbanthietbi_iot.views.favorite.FavoritesScreen
import com.example.ungdungbanthietbi_iot.views.signUp_signIn.ForgotPasswordScreen
import com.example.ungdungbanthietbi_iot.views.home.HomeScreen
import com.example.ungdungbanthietbi_iot.views.signUp_signIn.IntroScreen
import com.example.ungdungbanthietbi_iot.views.signUp_signIn.LoginScreen
import com.example.ungdungbanthietbi_iot.views.order_detail.OrderDetailsScreen
import com.example.ungdungbanthietbi_iot.views.product_detail.ProductDetailsScreen
import com.example.ungdungbanthietbi_iot.views.signUp_signIn.RegisterScreen
import com.example.ungdungbanthietbi_iot.views.signUp_signIn.ResetPasswordScreen
import com.example.ungdungbanthietbi_iot.views.search.SearchResultsScreen
import com.example.ungdungbanthietbi_iot.views.search.SearchScreen
import com.example.ungdungbanthietbi_iot.views.signUp_signIn.VerifyOTPScreen
import com.example.ungdungbanthietbi_iot.views.Setting.ChangePassword
import com.example.ungdungbanthietbi_iot.views.Setting.ContactScreen
import com.example.ungdungbanthietbi_iot.views.address.UpdateAddress
import com.example.ungdungbanthietbi_iot.views.check_out.CheckOutSuccessScreen
import com.example.ungdungbanthietbi_iot.views.notification.NotificationScreen
import com.example.ungdungbanthietbi_iot.views.personal.AccountSettingsScreen
import com.example.ungdungbanthietbi_iot.views.personal.EditEmailScreen
import com.example.ungdungbanthietbi_iot.views.personal.EditPhoneScreen
import com.example.ungdungbanthietbi_iot.views.personal.EditProfileScreen
import com.example.ungdungbanthietbi_iot.views.personal.EditUsername
import com.example.ungdungbanthietbi_iot.views.personal.OrderListScreen
import com.example.ungdungbanthietbi_iot.views.personal.PersonalScreen
import com.example.ungdungbanthietbi_iot.views.rating.ProductReviewsScreen
import com.example.ungdungbanthietbi_iot.views.rating.RatingHistoryScreen
import com.example.ungdungbanthietbi_iot.views.rating.RatingScreen
import com.example.ungdungbanthietbi_iot.views.rating.UpdateRatingScreen
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
    startDestination: String, // Thêm tham số startDestination động
    navController:NavHostController,
    deviceViewModel: DeviceViewModel,
    slideShowViewModel: SlideShowViewModel,
    imageViewModel: ImageViewModel,
    reviewViewModel: ReviewViewModel,
    accountViewModel: AccountViewModel,
    customerViewModel: CustomerViewModel
){
    NavHost(
        navController = navController,
        // Màn hình đầu tiên hiển thị
        startDestination = startDestination,
        enterTransition = {   // Khi màn hình mới xuất hiện
            scaleIn(
                initialScale = 0.8f, // màn hình bắt đầu nhỏ hơn 80% kích thước ban đầu
                transformOrigin = TransformOrigin(0.5f, 0.5f), // hiệu ứng phóng ở trung tâm
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {    // Khi màn hình hiện tại rời đi
            scaleOut(
                targetScale = 0.8f, // thu nhỏ còn 80% trước khi biến mất
                transformOrigin = TransformOrigin(0.5f, 0.5f), // thu nhỏ về giữa
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {    // Khi quay lại màn hình trước (pop back)
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {     // Khi rời màn hình hiện tại khi quay lại (pop back)
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ){
        // Màn hình IntroScreen sau khoảng thời gian quy định thì chuyển sang màn hình trang chủ HomeScreen
        composable(route = Screen.IntroScreen.route){
            IntroScreen(accountViewModel, navController)
        }

        //Home chưa đăng nhập
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController, deviceViewModel, slideShowViewModel, null, null)
        }

        //Home đã có tài khoản đăng nhập
        composable(route = Screen.HomeScreen.route + "?username={username}&id={id}",
            arguments = listOf(
                navArgument("username"){type = NavType.StringType },
                navArgument("id"){type = NavType.StringType }
            )
        ){
            val username = it.arguments?.getString("username")
            val id = it.arguments?.getString("id")
            HomeScreen(navController, deviceViewModel, slideShowViewModel, username, id)
        }

        //Màn hình đăng nhập
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController, accountViewModel)
        }

        //Màn hình thanh toán
        composable(
            route = Screen.Check_Out.route + "?selectedProducts={selectedProducts}&tongtien={tongtien}&username={username}&id={id}",
            arguments = listOf(
                navArgument("selectedProducts") {type = NavType.StringType },
                navArgument("tongtien") { type = NavType.StringType},
                navArgument("username") {type = NavType.StringType },
                navArgument("id") {type = NavType.StringType }
            )
        ){ backStackEntry ->
            // Lấy chuỗi selectedProducts từ tham số điều hướng
            val selectedProductsString = backStackEntry.arguments?.getString("selectedProducts")

            // Gọi hàm parseSelectedProducts để chuyển chuỗi thành danh sách Triple<Int, Int, Int>
            val selectedProducts = selectedProductsString?.let { parseSelectedProducts(it) } ?: emptyList()

            // Chuyển đổi tongtien từ String sang Int, nếu không có giá trị thì mặc định là 0
            val tongtien = backStackEntry.arguments?.getString("tongtien")?.toDoubleOrNull() ?: 0.0
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val id = backStackEntry.arguments?.getString("id") ?: ""

            CheckoutScreen(navController, selectedProducts, tongtien = tongtien, username = username, idCustomer = id)
        }
        //Màn hình đăng ký
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController, accountViewModel, customerViewModel)
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
            route = "${Screen.Address_Selection.route}?idCustomer={idCustomer}&selectedAddressId={selectedAddressId}",
            arguments = listOf(
                navArgument("idCustomer") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("selectedAddressId") {
                    type = NavType.StringType // Sử dụng StringType để hỗ trợ null
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val idCustomer = backStackEntry.arguments?.getString("idCustomer")
            val selectedAddressId = backStackEntry.arguments?.getString("selectedAddressId")?.toIntOrNull()
            AddressSelectionScreen(
                navController = navController,
                idCustomer = idCustomer,
                selectedAddressId = selectedAddressId
            )
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
                navArgument("idCustomer"){
                    type = NavType.StringType
                    defaultValue = ""},
                navArgument("username") {
                    type = NavType.StringType
                    defaultValue = ""
                }
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
            val idDevice = it.arguments?.getString("idDevice")
            if(idDevice != null){
                ProductReviewsScreen(navController, idDevice, reviewViewModel)
            }
        }

        //màn hình thanh toán
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
        composable(route = Screen.Rating_History.route + "?idCustomer={idCustomer}",
            arguments = listOf(
                navArgument("idCustomer") {type = NavType.StringType }
            )
        ) {
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            RatingHistoryScreen(navController, idCustomer)
        }

        //Màn hình thêm đánh giá, bình luận
        composable(route = Screen.Rating_Screen.route + "?idCustomer={idCustomer}&idDevice={idDevice}",
            arguments = listOf(
                navArgument("idCustomer") {type = NavType.StringType },
                navArgument("idDevice") {type = NavType.IntType }
            )
        ) {
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            val idDevice = it.arguments?.getInt("idDevice") ?: 0
            RatingScreen(navController, idCustomer, idDevice)
        }

        //Màn hình cập nhật đánh giá, bình luận
        composable(route = Screen.Update_Rating_Screen.route + "?idReview={idReview}&idCustomer={idCustomer}",
            arguments = listOf(
                navArgument("idReview") {type = NavType.IntType },
                navArgument("idCustomer") {type = NavType.StringType }
            )
        ) {
            val idReview = it.arguments?.getInt("idReview") ?: 0
            val idCustomer = it.arguments?.getString("idCustomer") ?: ""
            UpdateRatingScreen(navController, idReview, idCustomer)
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
            SearchScreen(navController, username)
        }

        //Màn hình tìm kiếm chưa có tài khoản
        composable(
            route = Screen.Search_Screen.route
        ){
            SearchScreen(navController, null)
        }

        //Màn hình kết quả tìm kiếm
        composable(
            route = Screen.Search_Results.route + "?query={query}&username={username}",
            arguments = listOf(
                navArgument("query") {type = NavType.StringType },
                navArgument("username") {type = NavType.StringType }
            )
        ){
            val query = it.arguments?.getString("query") ?: ""
            val username = it.arguments?.getString("username") ?: ""
            SearchResultsScreen(navController, query, username)
        }

        //Màn hình kết quả tìm kiếm chưa đăng nhập
        composable(
            route = Screen.Search_Results.route + "?query={query}",
            arguments = listOf(
                navArgument("query") {type = NavType.StringType },
            )
        ){
            val query = it.arguments?.getString("query") ?: ""
            SearchResultsScreen(navController, query, null)
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

        //Dẫn đến màn chọn(chỉnh sửa) Username
        composable(Screen.EditUsernamScreen.route + "/{id}/{username}") {
            backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val id = backStackEntry.arguments?.getString("id") ?: ""
            EditUsername(onBack = { navController.popBackStack()},id,username)
        }
        //Dẫn đến màn chọn(chỉnh sửa) SỐ ĐIỆN THOẠI
        composable(Screen.EditPhoneScreen.route + "/{id}/{phoneNumber}") {
            backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            EditPhoneScreen(id,onBack = { navController.popBackStack()}, phoneNumber)
        }
        //Dẫn đến màn chọn(chỉnh sửa) Email cá nhân
        composable(Screen.EditEmailScreen.route + "/{id}/{email}",) {
            backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EditEmailScreen(id,onBack = { navController.popBackStack()},email)
        }
        //Dẫn đến Man hinh Setting
        composable(Screen.SettingScreen.route +"/{idPerson}/{password}") {
            backStackEntry ->
            val idPerson = backStackEntry.arguments?.getString("idPerson") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            AccountSettingsScreen(navController,onBack = { navController.popBackStack()},idPerson,password)
        }
        //Dẫn đến màn hình liên hệ
        composable(Screen.ContactScreen.route) {
            ContactScreen(onBack = { navController.popBackStack() })
        }
        //Dẫn đến màn hình chỉnh sửa Mật khẩu
        composable(Screen.ChangePassword.route+ "/{id}/{password}") {
            backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            AccountSettingsScreen(navController,onBack = { navController.popBackStack()},id,password)
            ChangePassword(onBack = { navController.popBackStack()},id,password)
        }

        //màn hình thông tin cá nhân
        composable(
            Screen.PersonalScreen.route + "?username={username}&id={id}",
            arguments = listOf(
                navArgument("username") {type = NavType.StringType },
                navArgument("id") {type = NavType.StringType }
            )
        ) {
            val username = it.arguments?.getString("username") ?: ""
            val id = it.arguments?.getString("id") ?: ""
            PersonalScreen(navController, username, id, deviceViewModel)
        }

        //màn hình thông báo
        composable(
            Screen.Notification_Screen.route +"?idUser={idUser}",
            arguments = listOf(navArgument("idUser") {
                type = NavType.StringType
                nullable = true
                defaultValue = ""
            })
        ) {
            val idUser = it.arguments?.getString("idUser") ?: ""
            NotificationScreen(navController, idUser)
        }
    }
}