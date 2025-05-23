package com.example.ungdungbanthietbi_iot

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.data.account.AccountViewModel
import com.example.ungdungbanthietbi_iot.data.device.DeviceViewModel
import com.example.ungdungbanthietbi_iot.data.image_device.ImageViewModel
import com.example.ungdungbanthietbi_iot.data.review_device.ReviewViewModel
import com.example.ungdungbanthietbi_iot.data.slideshow.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.data.customer.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.NavGraph
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme
import kotlinx.coroutines.flow.first

// Đảm bảo DataStore được định nghĩa ở cấp cao nhất
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
class MainActivity : ComponentActivity() {
    private val deviceViewModel by viewModels<DeviceViewModel>()
    private val slideShowViewModel by viewModels<SlideShowViewModel>()
    private val imageViewModel by viewModels<ImageViewModel>()
    private val reviewViewModel by viewModels<ReviewViewModel>()
    private val accountViewModel by viewModels<AccountViewModel>()
    private val customerViewModel by viewModels<CustomerViewModel>()
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ép ứng dụng luôn ở chế độ sáng
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContent {
            UngDungBanThietBi_IOTTheme {
                navController = rememberNavController()
                val context = LocalContext.current
                // Biến trạng thái để kiểm tra đăng nhập
                var isChecking by remember { mutableStateOf(true) }
                var startDestination by remember { mutableStateOf(Screen.IntroScreen.route) }

                // Kiểm tra trạng thái đăng nhập
                LaunchedEffect(Unit) {
                    val preferences = context.dataStore.data.first()
                    val usernameKey = stringPreferencesKey("username")
                    val passwordKey = stringPreferencesKey("password")
                    val savedUsername = preferences[usernameKey]
                    val savedPassword = preferences[passwordKey]

                    if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                        // Thực hiện đăng nhập tự động
                        accountViewModel.CheckLogin(savedUsername, savedPassword)
                        accountViewModel.loginResult.collect { loginResult ->
                            if (loginResult != null) {
                                startDestination = if (loginResult.result == true) {
                                    "${Screen.HomeScreen.route}?username=$savedUsername"
                                } else {
                                    Screen.IntroScreen.route
                                }
                                isChecking = false
                                return@collect
                            }
                        }
                    } else {
                        // Nếu không có thông tin đăng nhập, điều hướng đến IntroScreen
                        startDestination = Screen.HomeScreen.route
                        isChecking = false
                    }
                }

                // Hiển thị màn hình "đang tải" trong khi kiểm tra
                if (isChecking) {
                    LoadingScreen()
                } else {
                    NavGraph(
                        startDestination = startDestination,
                        navController = navController,
                        deviceViewModel = deviceViewModel,
                        slideShowViewModel = slideShowViewModel,
                        imageViewModel = imageViewModel,
                        reviewViewModel = reviewViewModel,
                        accountViewModel = accountViewModel,
                        customerViewModel = customerViewModel
                    )
                }
            }
        }
    }
}
// Composable để hiển thị màn hình "đang tải"
@Composable
fun LoadingScreen() {
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
}
