package com.example.ungdungbanthietbi_iot.views.signUp_signIn

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.navigation.Screen
import com.example.ungdungbanthietbi_iot.viewModels.AccountViewModel
import com.example.ungdungbanthietbi_iot.views.components.CustomerTextField

/** Giao diện màn hình lấy lại mật khẩu (ForgotPasswordScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 12/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình lấy lại mật khẩu
 * ---------------------------------------------------------------------
 * Người cập nhật:Duy Trọng
 * --------------------------------------------------------------------
 * Ngày cập nhật:21/12/2024
 * ----------------------------------------------------------------------
 * Nội dung cập nhật:Chỉnh sửa lại TextField, layout
 *
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    accountViewModel: AccountViewModel
) {
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    // Lấy trạng thái từ ViewModel
    val isLoading by accountViewModel.isLoading.collectAsState()
    val sendOtpResult by accountViewModel.sendOtpResult.collectAsState()

    // Theo dõi kết quả gửi OTP
    LaunchedEffect(sendOtpResult) {
        sendOtpResult?.let { response ->
            if (response.isSuccessful) {
                showSuccessDialog = true
            } else {
                errorMessage = "Không tìm thấy email"
                showErrorDialog = true
            }
            // Reset trạng thái trong ViewModel để tránh lặp lại
            accountViewModel.resetSendOtpResult()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color(0xFF5D9EFF)
                ),
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Box(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.ill_forgot_password),
                contentDescription = "Forgot Password Illustration",
                modifier = Modifier
                    .weight(4.5f)
                    .padding(horizontal = 32.dp),
                contentScale = ContentScale.Fit,
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(5.5f)
            ) {
                Text(
                    text = "Quên\nMật khẩu ?", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D9EFF)
                    )
                )
                Text(
                    text = "Đừng lo! Điều đó xảy ra. Vui lòng nhập địa chỉ liên kết với tài khoản của bạn.",
                    style = MaterialTheme.typography.bodyMedium
                )
                CustomerTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    hint = "Email ",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Email Field",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                Button(
                    onClick = {
                        if (email.isNotEmpty()) {
                            accountViewModel.sendOtp(email)
                        } else {
                            errorMessage = "Vui lòng nhập email !"
                            showErrorDialog = true
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D9EFF),
                        disabledContainerColor = Color(0xFF5D9EFF)
                    ),
                    enabled = !isLoading // Vô hiệu hóa nút khi đang loading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 4.dp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Gửi yêu cầu",
                            fontSize = 18.sp
                        )
                    }
                }
                Box(modifier = Modifier.height(16.dp))
                // Error Dialog
                if (showErrorDialog) {
                    AlertDialog(
                        onDismissRequest = { showErrorDialog = false },
                        title = { Text("Lỗi") },
                        text = { Text(errorMessage) },
                        containerColor = Color.White,
                        confirmButton = {
                            Button(
                                onClick = { showErrorDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5D9EFF)
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Xác nhận")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showErrorDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Hủy")
                            }
                        }
                    )
                }
                if (showSuccessDialog) {
                    AlertDialog(
                        onDismissRequest = { showSuccessDialog = false },
                        title = { Text("Thông báo") },
                        containerColor = Color.White,
                        text = { Text(text = "Mã OTP đã được gửi đến email của bạn.\nVui lòng kiểm tra và nhập mã OTP vào ô bên dưới") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showSuccessDialog = false
                                    navController.navigate(Screen.VerifyOTPScreen.route + "?email=$email"){
                                        popUpTo(0) { inclusive = true }
                                    }
                                          },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5D9EFF)
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Tiếp tục")
                            }
                        }
                    )
                }
            }
        }
    }
}
