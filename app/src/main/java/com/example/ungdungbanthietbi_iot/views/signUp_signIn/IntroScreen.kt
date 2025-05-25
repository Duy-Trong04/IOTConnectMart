package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen

/** Giao diện màn Splash (IntroScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số onTimeout kiểu Unit
 *
 * Output: Chứa các thành phần giao diện của màn hình Splash
 *          sau 3s chuyển sang màn hình tramh chủ
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroScreen(accountViewModel: AccountViewModel, navController: NavController) {
//    // Hiệu ứng mờ dần cho logo và tiêu đề
//    val alpha = animateFloatAsState(
//        targetValue = 1f,
//        animationSpec = tween(
//            durationMillis = 2000 // 2 giây fade-in
//        )
//    ).value


    LaunchedEffect(Unit) {
        // Điều hướng đến LoginScreen ngay lập tức vì kiểm tra đăng nhập đã được thực hiện trong MainActivity
        navController.navigate(Screen.LoginScreen.route) {
            popUpTo(0) { inclusive = true }
        }
    }
    // UI của SplashScreen (có thể tùy chỉnh theo ý muốn)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Đang tải...",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF085979)
        )
    }
//    // Tạo một hiệu ứng delay để chuyển màn hình sau một khoảng thời gian nhất định (3 giây)
//    LaunchedEffect(Unit) {
//        delay(3000) // 3 giây
//        onTimeout() // Hàm này gọi khi thời gian hết, dùng để chuyển màn hình
//    }
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        content = { padding ->
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    // Hiển thị logo với hiệu ứng fade-in
//                    Image(
//                        painter = painterResource(id = R.drawable.logo), // Đảm bảo bạn có logo trong `res/drawable`
//                        contentDescription = "Logo",
//                        modifier = Modifier
//                            .size(320.dp)
//                            //.alpha(alpha) // Áp dụng hiệu ứng mờ dần
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Tiêu đề với hiệu ứng fade-in
//                    Text(
//                        text = "IOT Connect Mart",
//                        fontSize = 27.sp,
//                        color = Color(0xFF085979),
//                        fontWeight = FontWeight.Bold,
//                        //modifier = Modifier.alpha(alpha) // Áp dụng hiệu ứng mờ dần
//                    )
//                }
//            }
//        }
//    )
}
