package com.example.ungdungbanthietbi_iot.screen.signUp_signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.delay

/** Giao diện màn hình xác thực OTP (VerifyOTPScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 10/12/2024
 * Lần cập nhật cuối cùng: 13/12/2024
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình xác thực OTP
 * ------------------------------------------------------------
 * Người cập nhật:
 * Ngày cập nhật:
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun VerifyOTPScreen(navController: NavController) {
    // Biến nhận dữ liệu mã OTP từ người dùng
    var OTP by remember { mutableStateOf("") }
    var isResendEnabled by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(59) }
    val scope = rememberCoroutineScope()
    // Khởi tạo FocusRequester cho các trường nhập liệu
    val focusRequesterOTP = remember { FocusRequester() }

    // Bắt đầu đếm ngược thời gian
    LaunchedEffect(key1 = timer) {
        if (timer > 0) {
            delay(1000L) // Chờ 1 giây
            timer -= 1
        } else {
            isResendEnabled = true
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "XÁC THỰC OTP",
                        fontSize = 27.sp,
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )

                    // Hiển thị logo từ file drawable
                    Image(
                        // Thay "logo" bằng tên file ảnh
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(320.dp)
                    )
                    Text(
                        text = "IOT Connect Mart",
                        fontSize = 27.sp,
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    //Mã OTP
                    TextField(
                        value = OTP,
                        onValueChange = {
                            //Kiểm tra đầu vào chỉ nhận số và có độ dài đúng (mặc định là 6 ký tự).
                            if (it.length <= 6 && it.all { char -> char.isDigit() })
                            {
                                OTP = it
                            }},
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Mã OTP") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = {
                        isResendEnabled = false
                        timer = 59// Reset thời gian
                        //xử lý gửi lại mã OTP
                    },
                        enabled = isResendEnabled,
                    ) {
                        Text(text = if (timer > 0) "Gửi lại OTP sau ${timer}s" else "Gửi lại",
                            color = if(timer <= 0) Color(0xFF00C3FF) else Color.Black,
                        )
                    }
                    Button(
                        onClick = { /* Chuyển sang màn hình cập nhật mật khẩu(ResetPasswordScreen) */
                            navController.navigate(Screen.ResetPasswordScreen.route)
                        },
                        modifier = Modifier
                            .width(350.dp)
                            .padding(start = 10.dp)
                            .height(45.dp),
                        enabled = OTP.length == 6,
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C3FF))
                    ) {
                        Text(
                            text = "XÁC NHẬN",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        }
    )

}