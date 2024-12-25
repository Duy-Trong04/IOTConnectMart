package com.example.ungdungbanthietbi_iot.screen.signUp_signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.R
import com.example.ungdungbanthietbi_iot.navigation.Screen

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ForgotPasswordScreen(navController: NavController) {
    // Biến nhận dữ liệu email từ người dùng
    var email by remember { mutableStateOf("") }
    // Khởi tạo FocusRequester cho các trường nhập liệu
    val focusRequesterEmail = remember { FocusRequester() }
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
                        text = "KHÔI PHỤC MẬT KHẨU",
                        fontSize = 27.sp,
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )

                    // Hiển thị logo từ file drawable
                    Image(
                        // Thay "logo" bằng tên file ảnh
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(300.dp)
                    )

                    Text(
                        text = "IOT Connect Mart",
                        fontSize = 27.sp,
                        color = Color(0xFF085979),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    //email
                    TextField(
                        value = email,
                        onValueChange = {email = it},
                        modifier = Modifier.width(350.dp).padding(4.dp),
                        placeholder = { Text(text = "Email") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00C3FF)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        )
                    )

                    //Button
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Chuyển sang màn hình xác thực OTP(VerifyOTPScreen) */
                            navController.navigate(Screen.VerifyOTPScreen.route)
                        },
                        modifier = Modifier
                            .width(350.dp)
                            .padding(horizontal = 10.dp)
                            .height(45.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C3FF))
                    ) {
                        Text(text = "GỬI YÊU CẦU", fontSize = 23.sp, fontWeight = FontWeight.Bold)
                    }

                }
            }

        }
    )

}