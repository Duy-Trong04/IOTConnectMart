package com.example.ungdungbanthietbi_iot.screen.signUp_signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.navigation.Screen
import kotlinx.coroutines.delay

/** Giao diện màn hình xác thực OTP (VerifyOTPScreen)
 * -------------------------------------------
 * Người code: Văn Nam Cao
 * Ngày viết: 10/12/2024
 * Lần cập nhật cuối cùng: 15/05/2025
 * -------------------------------------------
 * Input: tham số navController kiểu NavController
 *
 * Output: Chứa các thành phần giao diện của màn hình xác thực OTP với các ô nhập riêng biệt
 * ------------------------------------------------------------
 * Người cập nhật: [Tên người cập nhật]
 * Ngày cập nhật: 15/05/2025
 * ------------------------------------------------------------
 * Nội dung cập nhật:
 * - Thiết kế lại giao diện OTP với từng ô nhập riêng, bo góc 5.dp, viền xanh
 * - Tự động chuyển focus sang ô tiếp theo sau khi nhập
 * - Hỗ trợ xóa và chuyển về ô trước đó
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOTPScreen(navController: NavController) {
    // Biến lưu trữ 6 chữ số OTP
    var otpDigits by remember { mutableStateOf(List(6) { "" }) }
    var isResendEnabled by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(59) }
    // Tạo FocusRequester cho từng ô
    val focusRequesters = remember { List(6) { FocusRequester() } }

    // Bắt đầu đếm ngược thời gian
    LaunchedEffect(key1 = timer) {
        if (timer > 0) {
            delay(1000L)
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

                    Image(
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Ô nhập OTP
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        otpDigits.forEachIndexed { index, digit ->
                            OTPDigitBox(
                                value = digit,
                                onValueChange = { newValue ->
                                    val newDigits = otpDigits.toMutableList()
                                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                        newDigits[index] = newValue
                                        otpDigits = newDigits
                                        // Chuyển focus sang ô tiếp theo nếu nhập xong
                                        if (newValue.isNotEmpty() && index < 5) {
                                            focusRequesters[index + 1].requestFocus()
                                        }
                                    }
                                },
                                focusRequester = focusRequesters[index],
                                onBackspace = {
                                    if (digit.isEmpty() && index > 0) {
                                        // Chuyển về ô trước nếu ô hiện tại rỗng
                                        focusRequesters[index - 1].requestFocus()
                                        val newDigits = otpDigits.toMutableList()
                                        newDigits[index - 1] = ""
                                        otpDigits = newDigits
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            isResendEnabled = false
                            timer = 59
                            otpDigits = List(6) { "" } // Reset OTP
                            focusRequesters[0].requestFocus() // Focus vào ô đầu tiên
                        },
                        enabled = isResendEnabled,
                    ) {
                        Text(
                            text = if (timer > 0) "Gửi lại OTP sau ${timer}s" else "Gửi lại",
                            color = if (timer <= 0) Color(0xFF00C3FF) else Color.Black,
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate(Screen.ResetPasswordScreen.route)
                        },
                        modifier = Modifier
                            .width(350.dp)
                            .padding(start = 10.dp)
                            .height(45.dp),
                        enabled = otpDigits.all { it.isNotEmpty() },
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

@Composable
fun OTPDigitBox(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onBackspace: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .border(2.dp, Color(0xFF00C3FF), RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .focusRequester(focusRequester)
                .onKeyEvent { event ->
                    if (event.key == Key.Backspace && value.isEmpty()) {
                        onBackspace()
                        true
                    } else {
                        false
                    }
                },
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester.requestFocus() }
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) {
                    innerTextField()
                }
            }
        )
    }
}