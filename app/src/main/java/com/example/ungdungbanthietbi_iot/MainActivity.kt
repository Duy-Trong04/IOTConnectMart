package com.example.ungdungbanthietbi_iot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ImageViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.viewModels.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.NavGraph
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.URLEncoder
import kotlin.math.log

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
    val _paymentStatus = MutableStateFlow<String?>(null)
    val paymentStatus: StateFlow<String?> = _paymentStatus
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
                var paymentNavigation by remember { mutableStateOf<String?>(null) }

                // Kiểm tra trạng thái đăng nhập
                LaunchedEffect(Unit) {
                    val preferences = context.dataStore.data.first()
                    val usernameKey = stringPreferencesKey("username")
                    val passwordKey = stringPreferencesKey("password")
                    val savedUsername = preferences[usernameKey]
                    val savedPassword = preferences[passwordKey]
                    val orderId = preferences[stringPreferencesKey("order_id")] ?: "N/A"
                    val totalMoney = preferences[intPreferencesKey("total_money")] ?: 0
                    val createdAt = preferences[stringPreferencesKey("created_at")] ?: ""
                    val status = preferences[stringPreferencesKey("payment_status")]

                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.d("FCM Notify", "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }

                            //Get new FCM registration token
                            val token: String? = task.result
                            Log.d("FCM Token", token, task.exception)
                            Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
                        })

                    if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                        // Thực hiện đăng nhập tự động
                        accountViewModel.checkLogin(savedUsername, savedPassword)
                        accountViewModel.loginUiState.collect { loginState ->
                            if (loginState.isLoading) return@collect
                            startDestination = if (loginState.result == true && loginState.customer_id != null) {
                                "${Screen.HomeScreen.route}?username=$savedUsername&id=${loginState.customer_id}&password=$savedPassword"
                            } else {
                                Screen.LoginScreen.route
                            }
                            if (status == "success" && loginState.result == true && loginState.customer_id != null) {
                                val encodedCreatedAt = URLEncoder.encode(createdAt, "UTF-8")
                                paymentNavigation = "${Screen.CheckOutSuccess.route}?username=$savedUsername&id=${loginState.customer_id}&orderId=$orderId&totalMoney=$totalMoney&createdAt=$encodedCreatedAt&password=$savedPassword"
                            }
                            isChecking = false
                            return@collect
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
                    // Điều hướng đến CheckOutSuccessScreen sau khi NavGraph được thiết lập
                    LaunchedEffect(paymentNavigation) {
                        if (paymentNavigation != null) {
                            navController.navigate(paymentNavigation!!) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            context.dataStore.edit {
                                it.remove(stringPreferencesKey("payment_status"))
                                it.remove(stringPreferencesKey("order_id"))
                                it.remove(intPreferencesKey("total_money"))
                                it.remove(stringPreferencesKey("created_at"))
                            }
                        }
                    }
                }
            }
        }
        handleDeepLink(intent)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }
    private suspend fun savePaymentStatus(context: Context, status: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("payment_status")] = status
        }
    }
    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { uri ->
            Log.d("MainActivity", "Deep Link URI: $uri")
            if (uri.scheme == "myapp" && uri.host == "payment") {
                val paymentStatus = uri.getQueryParameter("paymentStatus")
                val responseCode = uri.getQueryParameter("vnp_ResponseCode")
                Log.d("MainActivity", "PaymentStatus: $paymentStatus, ResponseCode: $responseCode")
                val status = when (responseCode) {
                    "00" -> "success"
                    "24" -> "cancelled"
                    "11" -> "expired"
                    else -> paymentStatus ?: "fail"
                }
                Log.e("MainActivity", "status: $status")
                _paymentStatus.value = status // Cập nhật StateFlow
                CoroutineScope(Dispatchers.IO).launch {
                    savePaymentStatus(applicationContext, status)
                }
                val broadcastIntent = Intent("com.example.ungdungbanthietbi_iot.CLOSE_CUSTOM_TABS")
                broadcastIntent.putExtra("paymentStatus", status)
                applicationContext.sendBroadcast(broadcastIntent)
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
