package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.launch
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ungdungbanthietbi_iot.api.SendTokenRequest
import com.example.ungdungbanthietbi_iot.dataStore
import com.example.ungdungbanthietbi_iot.viewModels.NoticeViewModel
import com.example.ungdungbanthietbi_iot.views.components.CustomerTextField
import com.google.firebase.messaging.FirebaseMessaging
import java.util.UUID

/** Giao diện màn hình đăng nhập (LoginScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình đăng nhập
 * ------------------------------------------------------------
 * Người cập nhật: Duy Trọng
 * Ngày cập nhật: 21/12/2024
 * ------------------------------------------------------------
 * Nội dung cập nhật:Chỉnh sửa lại TextField, layout
 *
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "HardwareIds",
    "CoroutineCreationDuringComposition"
)
@Composable
fun LoginScreen(navController: NavController, accountViewModel: AccountViewModel){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val noticeViewModel: NoticeViewModel = viewModel()
    // Observe loginUiState
    val loginUiState by accountViewModel.loginUiState.collectAsState()
    // Biến nhận dữ liệu email từ người dùng
    var username by remember { mutableStateOf("") }
    // Biến nhận dữ liệu password từ người dùng
    var password by remember { mutableStateOf("") }
    var passwordObscure by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    var openDialog by remember { mutableStateOf(false) }
    // Key cho DataStore
    val usernameKey = stringPreferencesKey("username")
    val passwordKey = stringPreferencesKey("password")
    val accessTokenKey = stringPreferencesKey("access_token")
    Scaffold {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Box(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.ill_signin),
                contentDescription = "Sign in Illustration",
                modifier = Modifier
                    .weight(3.5f)
                    .padding(
                        horizontal = 32.dp,
                    ),
                contentScale = ContentScale.Fit,
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(6.5f)
            ) {
                Text(
                    text = "Đăng nhập",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D9EFF)
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomerTextField(
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    hint = "Tài khoản",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Tài khoản",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomerTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    hint = "Mật khẩu",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Password,
                            contentDescription = "Mật khẩu",
                        )
                    },
                    trailingIcon = {
                        Icon(painter = if (passwordObscure) painterResource(id = R.drawable.ic_outline_visibility_off) else painterResource(
                            id = R.drawable.ic_outline_visibility
                        ), contentDescription = "Show Password", modifier = Modifier.clickable {
                            passwordObscure = !passwordObscure
                        }
                        )
                    },
                    obscure = passwordObscure,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    onClick = {
                        /* Chuyển sang màn hình quên mật khẩu(ForgotPasswordScreen)*/
                        navController.navigate(Screen.ForgotPasswordScreen.route)
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF5D9EFF)
                    )
                ) {
                    Text("Quên mật khẩu ?")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        /* Chuyển sang màn hình trang chủ(HomeScreen) */
                        if(username.isEmpty() || password.isEmpty()){
                            openDialog = true
                        }
                        else {
//                            accountViewModel.login(
//                                context = context,
//                                username = username,
//                                password = password
//                            )
                            accountViewModel.checkLogin(username, password)
                            scope.launch {
                                // Lắng nghe kết quả từ loginResult
                                accountViewModel.loginUiState.collect { state ->
                                    if (state.isLoading) return@collect
                                    if (state.result == true) {
                                        context.dataStore.edit { preferences ->
                                            preferences[usernameKey] = username
                                            preferences[passwordKey] = password
                                            preferences[accessTokenKey] = state.accessToken ?: ""
                                        }
                                        // Lấy FCM token và gửi lên server
                                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val fcmToken = task.result
                                                Log.d("FCM Token", fcmToken, task.exception)
                                                val tokenFCM = SendTokenRequest(
                                                    deviceToken = fcmToken
                                                )
                                                noticeViewModel.sendTokenToServer(tokenFCM, context)
                                                Log.d("FCM Token", "Success")
                                            } else {
                                                Log.d("FCM Token", "Failed to get FCM token", task.exception)
                                            }
                                        }
                                        navController.navigate(Screen.HomeScreen.route + "?username=$username&id=${state.customer_id}&token=${state.accessToken}") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else if (state.result == false) {
                                        openDialog = true
                                    }
                                    return@collect
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    )
                ) {
                    if (loginUiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = "Đăng nhập",
                            fontSize = 18.sp
                        )
                    }
                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight(),
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Divider(
//                        modifier = Modifier.weight(1f)
//                    )
//                    Text(
//                        "HOẶC",
//                        style = MaterialTheme.typography.bodyMedium,
//                        modifier = Modifier.padding(horizontal = 16.dp)
//                    )
//                    Divider(
//                        modifier = Modifier.weight(1f)
//                    )
//                }
//                OutlinedButton(
//                    onClick = {},
//                    shape = RoundedCornerShape(16.dp),
//                    modifier = Modifier
//                        .height(48.dp)
//                        .fillMaxWidth(),
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_google),
//                        contentDescription = "",
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                    Box(modifier = Modifier.width(32.dp))
//                    Text(
//                        text = "Đăng nhập với Google",
//                        style = MaterialTheme.typography.bodyLarge.copy(Color.Black)
//                    )
//                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        "Bạn chưa có tài khoản?",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Box(modifier = Modifier.width(8.dp))
                    Text(
                        "Đăng ký !",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF5D9EFF),
                        ),
                        modifier = Modifier.clickable {
                            /* Chuyển sang màn hình đăng ký(RegisterScreen) */
                            navController.navigate(Screen.RegisterScreen.route)
                        }
                    )
                }
            }
        }
    }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Text(text = "Thông báo")
            },
            text = {
                if (username.isEmpty() || password.isEmpty()) {
                    Text("Vui lòng nhập đầy đủ thông tin !")
                } else {
                    Text("Tài khoản hoặc mật khẩu không chính xác")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(15.dp),
            confirmButton = {
                Button(
                    onClick = { openDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Xác nhận")
                }
            }
        )
    }
}