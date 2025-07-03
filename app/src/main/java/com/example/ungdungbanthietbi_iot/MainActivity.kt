package com.example.ungdungbanthietbi_iot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ungdungbanthietbi_iot.api.RefreshRequest
import com.example.ungdungbanthietbi_iot.api.SendTokenRequest
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ImageViewModel
import com.example.ungdungbanthietbi_iot.viewModels.ReviewViewModel
import com.example.ungdungbanthietbi_iot.viewModels.SlideShowViewModel
import com.example.ungdungbanthietbi_iot.viewModels.CustomerViewModel
import com.example.ungdungbanthietbi_iot.navigation.NavGraph
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.ui.theme.UngDungBanThietBi_IOTTheme
import com.example.ungdungbanthietbi_iot.viewModels.NoticeViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.URLEncoder

// Đảm bảo DataStore được định nghĩa ở cấp cao nhất
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
class MainActivity : ComponentActivity() {
    private val deviceViewModel by viewModels<DeviceViewModel>()
    private val slideShowViewModel by viewModels<SlideShowViewModel>()
    private val imageViewModel by viewModels<ImageViewModel>()
    private val reviewViewModel by viewModels<ReviewViewModel>()
    private val accountViewModel by viewModels<AccountViewModel>()
    private val noticeViewModel by viewModels<NoticeViewModel>()
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
                NavGraph(
                    startDestination = Screen.IntroScreen.route,
                    navController = navController,
                    deviceViewModel = deviceViewModel,
                    slideShowViewModel = slideShowViewModel,
                    imageViewModel = imageViewModel,
                    reviewViewModel = reviewViewModel,
                    accountViewModel = accountViewModel,
                    customerViewModel = customerViewModel,
                    splashDuration = 2000L
                )
               //var startDestination by remember { mutableStateOf(Screen.IntroScreen.route) }
                var paymentNavigation by remember { mutableStateOf<String?>(null) }

                // Kiểm tra trạng thái đăng nhập
                LaunchedEffect(Unit) {
                    val preferences = context.dataStore.data.first()
                    val orderId = preferences[stringPreferencesKey("order_id")] ?: "N/A"
                    val totalMoney = preferences[intPreferencesKey("total_money")] ?: 0
                    val createdAt = preferences[stringPreferencesKey("created_at")] ?: ""
                    val status = preferences[stringPreferencesKey("payment_status")]
                    val usernameKey = stringPreferencesKey("username")
                    val passwordKey = stringPreferencesKey("password")
                    val refreshTokenKey = stringPreferencesKey("refresh_token")
                    val accessTokenKey = stringPreferencesKey("access_token")
                    val customerIdKey = stringPreferencesKey("customer_id")
                    val savedUsername = preferences[usernameKey]
                    val savedPassword = preferences[passwordKey]
                    val savedRefreshToken = preferences[refreshTokenKey]
                    val accessToken = preferences[accessTokenKey]
                    val savedCustomerId = preferences[customerIdKey]
                    if (!savedRefreshToken.isNullOrEmpty()) {
                        val request = RefreshRequest(
                            refreshToken = savedRefreshToken
                        )
                        accountViewModel.refreshToken(context, request)
                        accountViewModel.loginUiState.collect { loginState ->
                            if (loginState.isLoading) return@collect
                            if (loginState.result == true && loginState.accessToken != null) {
                                if (status == "success") {
                                    val encodedCreatedAt = URLEncoder.encode(createdAt, "UTF-8")
                                    paymentNavigation = "${Screen.CheckOutSuccess.route}?username=$savedUsername&id=${savedCustomerId}&orderId=$orderId&totalMoney=$totalMoney&createdAt=$encodedCreatedAt&token=${loginState.accessToken}"
                                } else {
                                    paymentNavigation = "${Screen.HomeScreen.route}?username=$savedUsername&id=${savedCustomerId}&token=${loginState.accessToken}"
                                }
                            } else if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                                accountViewModel.login(context, savedUsername, savedPassword)
                                accountViewModel.loginUiState.collect { retryState ->
                                    if (!retryState.isLoading && retryState.result == true && retryState.accessToken != null) {
                                        if (status == "success") {
                                            val encodedCreatedAt = URLEncoder.encode(createdAt, "UTF-8")
                                            paymentNavigation = "${Screen.CheckOutSuccess.route}?username=$savedUsername&id=${savedCustomerId}&orderId=$orderId&totalMoney=$totalMoney&createdAt=$encodedCreatedAt&token=${retryState.accessToken}"
                                        } else {
                                            paymentNavigation = "${Screen.HomeScreen.route}?username=$savedUsername&id=${savedCustomerId}&token=${retryState.accessToken}"
                                        }
                                    } else {
                                        // Nếu đăng nhập thất bại, điều hướng về LoginScreen
                                        paymentNavigation = Screen.LoginScreen.route
                                    }
                                }
                            }else {
                                // Nếu không có thông tin đăng nhập, điều hướng về LoginScreen
                                paymentNavigation = Screen.LoginScreen.route
                            }
                        }
                    }else {
                        // Nếu không có thông tin đăng nhập, điều hướng về LoginScreen
                        paymentNavigation = Screen.LoginScreen.route
                    }
                    // Gửi FCM Token chỉ khi đã đăng nhập
                    if (!savedUsername.isNullOrEmpty() && !accessToken.isNullOrEmpty()) {
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val fcmToken = task.result
                                Log.d("FCM Token", "FCM Token: $fcmToken")
                                val tokenFCM = SendTokenRequest(deviceToken = fcmToken)
                                noticeViewModel.sendTokenToServer(tokenFCM, context)
                            } else {
                                Log.d("FCM Notify", "Fetching FCM registration token failed", task.exception)
                            }
                        }
                    }

//                    if (status == "success") {
//
//                        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
//                            accountViewModel.checkLogin(savedUsername, savedPassword)
//                            //accountViewModel.login(context, savedUsername, savedPassword)
//                            accountViewModel.loginUiState.collect { loginState ->
//                                if (loginState.isLoading) return@collect
//                                if (loginState.result == true && loginState.customer_id != null) {
//                                    val encodedCreatedAt = URLEncoder.encode(createdAt, "UTF-8")
//                                    paymentNavigation = "${Screen.CheckOutSuccess.route}?username=$savedUsername&id=${loginState.customer_id}&orderId=$orderId&totalMoney=$totalMoney&createdAt=$encodedCreatedAt&token=${accessToken}"
//                                }
//                            }
//                        }
//                    }
                }
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
