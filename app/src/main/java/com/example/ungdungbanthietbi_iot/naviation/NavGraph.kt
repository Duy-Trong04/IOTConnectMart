package com.example.ungdungbanthietbi_iot.naviation

import androidx.compose.material3.DatePicker
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ungdungbanthietbi_iot.screen.Setting.ChangePassword
import com.example.ungdungbanthietbi_iot.screen.Setting.ContactScreen
import com.example.ungdungbanthietbi_iot.screen.personal.AccountSettingsScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditEmailScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditPhoneScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditProfileScreen
import com.example.ungdungbanthietbi_iot.screen.personal.EditUsername
import com.example.ungdungbanthietbi_iot.screen.personal.OrderListScreen
import com.example.ungdungbanthietbi_iot.screen.personal.PersonalScreen

@Composable
fun NavGraph(navController: NavHostController) {
    //Màn hình bắt đầu là hồ sơ cá nhân
    NavHost(
        navController = navController,
        startDestination = Screen.PersonalScreen.route
    ) {
        //Cac man hinh Personal( Ho so ca nhan)
        //Màn hình Personal( Hồ sơ cá nhân)
        composable(Screen.PersonalScreen.route) {
            PersonalScreen(navController)
        }
        //Đến màn Chỉnh sửa thông tin cá nhân
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(navController,onBack = { navController.popBackStack() })
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
    }
}
