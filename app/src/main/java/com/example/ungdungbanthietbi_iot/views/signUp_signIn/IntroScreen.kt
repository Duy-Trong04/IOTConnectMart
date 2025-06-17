package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.dataStore
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

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
@Composable
fun IntroScreen(accountViewModel: AccountViewModel, navController: NavController, splashDuration: Long = 2000L) {
    // Hiệu ứng phóng to cho logo
    val scale = remember { Animatable(0.5f) }

    // Hiệu ứng mờ dần cho tiêu đề
    val alpha = remember { Animatable(0f) }

    val context = LocalContext.current
    var destination by remember { mutableStateOf(Screen.LoginScreen.route) }
    var shouldNavigate by remember { mutableStateOf(false) }
    // Kiểm tra trạng thái đăng nhập và lần đầu mở ứng dụng
    LaunchedEffect(Unit) {
        // Bắt đầu hiệu ứng phóng to và mờ dần
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000)
        )
//        alpha.animateTo(
//            targetValue = 1f,
//            animationSpec = tween(durationMillis = 2000)
//        )

        // Kiểm tra trạng thái đăng nhập
        val preferences = context.dataStore.data.first()
        val usernameKey = stringPreferencesKey("username")
        val passwordKey = stringPreferencesKey("password")
        val isFirstLaunchKey = booleanPreferencesKey("is_first_launch")

        val savedUsername = preferences[usernameKey]
        val savedPassword = preferences[passwordKey]
        val isFirstLaunch = preferences[isFirstLaunchKey] ?: true

        // Nếu là lần đầu mở ứng dụng, lưu trạng thái
        if (isFirstLaunch) {
            context.dataStore.edit { prefs ->
                prefs[isFirstLaunchKey] = false
            }
        }

        // Kiểm tra đăng nhập với thời gian chờ tối đa
        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            accountViewModel.checkLogin(savedUsername, savedPassword)
            withTimeoutOrNull(2000L) { // Chờ tối đa 5 giây
                accountViewModel.loginUiState.collect { loginState ->
                    if (!loginState.isLoading && loginState.result == true && loginState.customer_id != null) {
                        destination = "${Screen.HomeScreen.route}?username=$savedUsername&id=${loginState.customer_id}&password=$savedPassword"
                    }
                    shouldNavigate = true
                }
            }
        } else {
            shouldNavigate = true
        }
        // Đảm bảo chờ đủ thời gian splash nếu chưa có kết quả đăng nhập
        if (!shouldNavigate) {
            delay(splashDuration)
            shouldNavigate = true
        }

        // Điều hướng khi sẵn sàng
        if (shouldNavigate) {
            navController.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF3D8CF0))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    // Logo với hiệu ứng phóng to
                    Image(
                        painter = painterResource(id = R.drawable.ecom_logo),
                        contentDescription = "Logo HomeConnect",
                        modifier = Modifier
                            .size(320.dp)
                            .scale(scale.value)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

//                    // Tiêu đề với hiệu ứng mờ dần
//                    Text(
//                        text = "HomeConnect E-com Store",
//                        fontSize = 27.sp,
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.alpha(alpha.value)
//                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    )
}
